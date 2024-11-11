package kr.re.kh.service.impl;

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

