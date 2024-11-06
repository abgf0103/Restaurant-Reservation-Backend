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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 리뷰 관련 API를 처리하는 Controller
 */
@RestController
@RequestMapping("/api/review")
@Slf4j
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 목록 조회
     * @param request 검색 조건
     * @return 리뷰 목록과 카운트
     */
    @ApiOperation("리뷰 목록 조회")
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewList(@ModelAttribute SearchHelper request) {
        return ResponseEntity.ok(reviewService.selectReview(request));
    }

    /**
     * 리뷰 상세 조회
     * @param id 리뷰 ID
     * @return 리뷰 상세 정보
     */
    @ApiOperation("리뷰 상세 조회")
    @GetMapping("/view/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewView(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.reviewInfo(id));
    }

    /**
     * 리뷰 저장
     * @param currentUser 인증된 사용자
     * @param reviewRequest 리뷰 요청 데이터
     * @return 저장된 결과
     */
    @ApiOperation("리뷰 저장")
    @PostMapping("/save")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewSave(@CurrentUser CustomUserDetails currentUser, @RequestBody ReviewRequest reviewRequest) {
        // 인증된 사용자 정보와 리뷰 요청을 기반으로 리뷰 저장
        reviewService.saveReview(currentUser, reviewRequest);
        return ResponseEntity.ok(new ApiResponse(true, "저장 되었습니다."));
    }

    /**
     * 리뷰 삭제
     * @param id 리뷰 ID
     * @return 삭제된 결과
     */
    @ApiOperation("리뷰 삭제")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewDelete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse(true, "삭제 되었습니다."));
    }
}
