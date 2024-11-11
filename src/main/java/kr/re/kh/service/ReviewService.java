package kr.re.kh.service;

import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.ReviewRequest;
import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {

    HashMap<String, Object> selectReview(SearchHelper searchHelper);

    void saveReview(CustomUserDetails currentUser, ReviewRequest reviewRequest);

    HashMap<String, Object> reviewInfo(Long reviewId);

    void deleteReview(Long reviewId);

    public List<Review> getReviewsByUserId(Long userId);


}
