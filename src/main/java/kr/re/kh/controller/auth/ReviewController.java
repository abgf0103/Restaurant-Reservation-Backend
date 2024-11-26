package kr.re.kh.controller.auth;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.ReviewRequest;
import kr.re.kh.model.payload.response.ApiResponse;
import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;
import kr.re.kh.service.ReviewService;
import kr.re.kh.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리뷰 관련 API를 처리하는 Controller
 */
@RestController
@RequestMapping("/api/review")
@Slf4j
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private final UserService userService;


    @ApiOperation("리뷰 목록 조회")
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewList(@ModelAttribute SearchHelper request) {
        return ResponseEntity.ok(reviewService.selectReview(request));
    }

    @ApiOperation("리뷰 상세 조회")
    @GetMapping("/view/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewView(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.reviewInfo(id));
    }

    @ApiOperation("내가 작성한 리뷰 목록 조회")
    @GetMapping("/my-reviews")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> getMyReviews(@CurrentUser CustomUserDetails currentUser) {
        Long userId = currentUser.getId();
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    //  username으로 특정 사용자의 리뷰 조회
    @ApiOperation("특정 사용자 리뷰 목록 조회 (username 기준)")
    @GetMapping("/view-by-username/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> getReviewsByUsername(@PathVariable String username) {
        log.info("Fetching reviews for username: " + username); // 로그 추가
        return ResponseEntity.ok(reviewService.getReviewsByUsername(username));
    }

    @ApiOperation("리뷰 수정")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewRequest reviewRequest) {
        reviewService.updateReview(id, reviewRequest);
        return ResponseEntity.ok(new ApiResponse(true, "리뷰가 수정되었습니다."));
    }



    @ApiOperation("리뷰 저장")
    @PostMapping("/save")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewSave(@CurrentUser CustomUserDetails currentUser, @RequestBody ReviewRequest reviewRequest) {
        Long userId = currentUser.getId();
        Long storeId = reviewRequest.getStoreId();
        Long reserveId = reviewRequest.getReserveId(); // 클라이언트로부터 전달받은 reserveId

        // 예약 상태 확인 (리뷰를 작성할 수 있는 상태인지 체크)
        boolean canWriteReview = reviewService.reserveStatusCheck(userId, storeId, reserveId);
        if (!canWriteReview) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "리뷰 작성 전에 해당 매장의 예약 상태를 확인하세요. 예약 상태가 2여야 합니다."));
        }

        // 리뷰가 이미 존재하는지 중복 체크
        boolean isReviewExist = reviewService.isReviewExist(userId, storeId, reserveId);
        if (isReviewExist) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "이미 리뷰를 작성한 예약입니다."));
        }

        // 리뷰 저장 로직
        //reviewService.saveReview(currentUser, reviewRequest);
        return ResponseEntity.ok(reviewService.saveReview(currentUser, reviewRequest));
    }

    @ApiOperation("리뷰 삭제")
    @DeleteMapping("/delete/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId,
                                          @RequestParam Long reserveId, // 쿼리 파라미터로 reserveId 받기
                                          @RequestParam Long userId, // 쿼리 파라미터로 userId 받기
                                          @CurrentUser CustomUserDetails currentUser) {

        // 요청을 받았을 때 로그 출력
        System.out.println("리뷰 삭제 요청 received");
        System.out.println("리뷰 ID: " + reviewId);
        System.out.println("예약 ID: " + reserveId);
        System.out.println("사용자 ID: " + userId);
        System.out.println("현재 사용자 ID (JWT): " + currentUser.getId());

        // 사용자 정보 검증
        if (!currentUser.getId().equals(userId)) {
            Long isAdmin = userService.isAdminByUserId(userId);
            log.info(isAdmin.toString());
            log.info(currentUser.getRoles().toString());
            if (isAdmin != 2L && isAdmin != 3L) {
                return ResponseEntity.status(403).body(new ApiResponse(false, "사용자 권한이 일치하지 않습니다."));
            }
        }

        try {
            reviewService.deleteReview(reviewId, reserveId, userId);
            return ResponseEntity.ok(new ApiResponse(true, "리뷰가 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생 시 스택 트레이스를 출력하여 디버깅
            return ResponseEntity.status(500).body(new ApiResponse(false, "리뷰 삭제에 실패했습니다."));
        }
    }



    @ApiOperation("리뷰 좋아요")
    @PostMapping("/like/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> likeReview(@PathVariable Long reviewId, @CurrentUser CustomUserDetails currentUser) {
        Long userId = currentUser.getId(); // 로그인된 사용자 ID

        // 리뷰 좋아요 처리
        boolean result = reviewService.likeReview(reviewId, userId);
        return ResponseEntity.ok(new ApiResponse(result, result ? "좋아요 처리되었습니다." : "이미 추가한 좋아요 입니다."));
    }

    @ApiOperation("리뷰 좋아요 취소")
    @DeleteMapping("/unlike/{reviewId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> unlikeReview(@PathVariable Long reviewId, @CurrentUser CustomUserDetails currentUser) {
        Long userId = currentUser.getId(); // 로그인된 사용자 ID

        try {
            // 리뷰 좋아요 취소 처리
            reviewService.unlikeReview(reviewId, userId);
            return ResponseEntity.ok(new ApiResponse(true, "좋아요 취소되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "좋아요 취소 중 오류 발생: " + e.getMessage()));
        }
    }

    @ApiOperation("리뷰 좋아요 상태 확인")
    @GetMapping("/likes/status")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> getLikeStatus(@RequestParam Long reviewId, @CurrentUser CustomUserDetails currentUser) {
        Long userId = currentUser.getId(); // 로그인된 사용자의 ID

        // 좋아요 상태 확인
        boolean isLiked = reviewService.isReviewLikedByUser(reviewId, userId);

        return ResponseEntity.ok(isLiked);
    }



    // 예약 상태 체크 (리뷰 작성 여부 확인)
    @ApiOperation("리뷰 작성 가능 여부 체크")
    @GetMapping("/check-reserve-status")
    public ResponseEntity<ApiResponse> checkReserveStatus(@RequestParam Long storeId, @RequestParam Long userId ,@RequestParam Long reserveId) {
        boolean canWriteReview = reviewService.reserveStatusCheck(userId, storeId, reserveId);
        if (!canWriteReview) {
            return ResponseEntity.ok(new ApiResponse(false, "리뷰 작성 전에 해당 매장의 예약 상태를 확인하세요. 예약 상태가 완료(2)이어야 합니다."));
        }
        return ResponseEntity.ok(new ApiResponse(true, "리뷰 작성 가능"));
    }

    @ApiOperation("중복 리뷰 체크")
    @GetMapping("/check-exist")
    public ResponseEntity<Boolean> checkExist(@RequestParam Long storeId, @RequestParam Long userId, @RequestParam Long reserveId) {
        boolean isExist = reviewService.isReviewExist(userId, storeId, reserveId);
        return ResponseEntity.ok(isExist);
    }

    @ApiOperation("가게ID로 리뷰 평균 별점 조회")
    @GetMapping("/getRatingAvgByStoreId")
    public double getRatingAvgByStoreId(@RequestParam("storeId") Long storeId) {
        return reviewService.getRatingAvgByStoreId(storeId);
    }

    @ApiOperation("가게ID로 리뷰 개수 조회")
    @GetMapping("/getReviewCountByStoreId")
    public long getReviewCountByStoreId(@RequestParam("storeId") Long storeId) {
        return reviewService.getReviewCountByStoreId(storeId);
    }

    @ApiOperation("리뷰 삭제(어드민)")
    @DeleteMapping("/deleteReviewForAdmin")
    public void deleteReviewForAdmin(@RequestParam Long reviewId) {   //리뷰 삭제(어드민)
        reviewService.deleteReviewForAdmin(reviewId);
    }
}


