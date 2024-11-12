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
        reviewService.saveReview(currentUser, reviewRequest);
        return ResponseEntity.ok(new ApiResponse(true, "저장 되었습니다."));
    }

    @ApiOperation("리뷰 삭제")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> reviewDelete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse(true, "삭제 되었습니다."));
    }
}
