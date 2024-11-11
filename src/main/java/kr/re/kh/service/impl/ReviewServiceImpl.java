package kr.re.kh.service.impl;

import kr.re.kh.exception.ResourceNotFoundException;
import kr.re.kh.mapper.ReviewMapper;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.ReviewRequest;
import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;
import kr.re.kh.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Override
    public HashMap<String, Object> selectReview(SearchHelper searchHelper) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("data", reviewMapper.selectReview(searchHelper));
        response.put("count", reviewMapper.countReview(searchHelper));
        return response;
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewMapper.selectReviewsByUserId(userId);
    }

    @Override
    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {
        // 해당 reviewId를 찾아서 Review를 업데이트
        Review review = reviewMapper.reviewInfo(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        review.setRating(reviewRequest.getRating());
        review.setReviewComment(reviewRequest.getReviewComment());
        review.setStoreId(reviewRequest.getStoreId());

        reviewMapper.updateReview(review);
    }





    @Override
    public void saveReview(CustomUserDetails currentUser, ReviewRequest reviewRequest) {
        Long userID = currentUser.getId();
        String username = currentUser.getUsername();  // USERNAME 가져오기
        Review review = Review.builder()
                .userId(userID)
                .storeId(reviewRequest.getStoreId())
                .rating(reviewRequest.getRating())
                .reviewComment(reviewRequest.getReviewComment())
                .username(username)  // USERNAME 저장
                .build();

        log.info(reviewRequest.toString());
        log.info(review.toString());
        reviewMapper.reviewSave(review);
    }

    @Override
    public HashMap<String, Object> reviewInfo(Long reviewId) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<Review> review = reviewMapper.reviewInfo(reviewId);
        review.ifPresent(value -> response.put("data", value));
        return response;
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewMapper.deleteReview(reviewId);
    }
}

