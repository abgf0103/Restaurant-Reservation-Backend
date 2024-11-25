package kr.re.kh.service;

import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.ReviewRequest;
import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {

    HashMap<String, Object> selectReview(SearchHelper searchHelper);

    List<HashMap<String, Object>> saveReview(CustomUserDetails currentUser, ReviewRequest reviewRequest);

    HashMap<String, Object> reviewInfo(Long reviewId);

    void deleteReview(Long reviewId, Long userId, Long reserveId);

    List<Review> getReviewsByUserId(Long userId);

    HashMap<String, Object> getReviewsByUsername(String username);

    void updateReview(Long reviewId, ReviewRequest reviewRequest);

    boolean likeReview(Long reviewId, Long userId);

    void unlikeReview(Long reviewId, Long userId);

    boolean isReviewLikedByUser(Long reviewId, Long userId);

    boolean reserveStatusCheck(Long userId, Long storeId, Long reserveId);  // 예약 상태 체크 메서드 추가

    boolean isReviewExist(Long userId, Long storeId, Long reserveId);  // 중복 리뷰 확인 메서드 추가

    double getRatingAvgByStoreId(Long storeId); //가게ID로 리뷰 평균 별점 조회

    Long getReviewCountByStoreId(Long storeId); //가게ID로 리뷰 개수 조회
}

