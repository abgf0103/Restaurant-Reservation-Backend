package kr.re.kh.controller.auth;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.ReviewRequest;
import kr.re.kh.model.payload.response.ApiResponse;
import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;
import kr.re.kh.service.ReviewService;
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
@CrossOrigin(origins = "http://localhost:3000") // React 앱의 URL
@Slf4j
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation("리뷰 목록 조회")
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewList(@ModelAttribute SearchHelper request) {
        return ResponseEntity.ok(reviewService.selectReview(request));
    }

    @ApiOperation("내가 작성한 리뷰 목록 조회")
    @GetMapping("/my-reviews")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> getMyReviews(@CurrentUser CustomUserDetails currentUser) {
        Long userId = currentUser.getId();
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation("리뷰 수정")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewRequest reviewRequest) {
        reviewService.updateReview(id, reviewRequest);
        return ResponseEntity.ok(new ApiResponse(true, "리뷰가 수정되었습니다."));
    }

    @ApiOperation("리뷰 상세 조회")
    @GetMapping("/view/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewView(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.reviewInfo(id));
    }

    @ApiOperation("리뷰 저장")
    @PostMapping("/save")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewSave(@CurrentUser CustomUserDetails currentUser, @RequestBody ReviewRequest reviewRequest) {
        Long userId = currentUser.getId();
        Long storeId = reviewRequest.getStoreId();

        // 예약 상태 확인 (리뷰를 작성할 수 있는 상태인지 체크)
        boolean canWriteReview = reviewService.reserveStatusCheck(userId, storeId);
        if (!canWriteReview) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "리뷰 작성 전에 해당 매장의 예약 상태를 확인하세요. 예약 상태가 2여야 합니다."));
        }

        // 리뷰 저장 로직
        reviewService.saveReview(currentUser, reviewRequest);
        return ResponseEntity.ok(new ApiResponse(true, "저장 되었습니다."));
    }

    @ApiOperation("리뷰 삭제")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> deleteReview(@PathVariable Long id,
                                          @RequestParam Long userId, // 쿼리 파라미터로 userId 받기
                                          @CurrentUser CustomUserDetails currentUser) {
        // 요청을 받았을 때 로그 출력
        System.out.println("리뷰 삭제 요청 received");
        System.out.println("리뷰 ID: " + id);
        System.out.println("사용자 ID: " + userId);
        System.out.println("현재 사용자 ID (JWT): " + currentUser.getId());


        // 사용자 정보 검증
        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(403).body(new ApiResponse(false, "사용자 권한이 일치하지 않습니다."));
        }

        try {
            reviewService.deleteReview(id, userId);
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

    //  username으로 특정 사용자의 리뷰 조회
    @ApiOperation("특정 사용자 리뷰 목록 조회 (username 기준)")
    @GetMapping("/view-by-username/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> getReviewsByUsername(@PathVariable String username) {
        log.info("Fetching reviews for username: " + username); // 로그 추가
        return ResponseEntity.ok(reviewService.getReviewsByUsername(username));
    }

    // 예약 상태 체크 (storeId, userId를 받아서 예약 상태가 '완료'인지 확인)
    @GetMapping("/check-reserve-status")
    public ResponseEntity<Boolean> checkReserveStatus(@RequestParam Long storeId, @RequestParam Long userId) {
        try {
            // 예약 상태가 완료(2)인 경우만 리뷰 작성 가능
            boolean canWriteReview = reviewService.reserveStatusCheck(userId, storeId);
            return ResponseEntity.ok(canWriteReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }


    }
}


