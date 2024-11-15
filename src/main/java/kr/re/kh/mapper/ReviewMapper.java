package kr.re.kh.mapper;

import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewMapper {

    List<Review> selectReview(SearchHelper searchHelper);

    int countReview(SearchHelper searchHelper);

    void reviewSave(Review review);  // REVIEW에 USERNAME 포함

    Optional<Review> reviewInfo(Long reviewId);

    void updateReview(Review review);

    // 리뷰 좋아요 삭제
    void deleteReviewLikes(Long reviewId);

    // 리뷰 삭제
    void deleteReview(Long reviewId);

    List<Review> selectReviewsByUserId(Long userId);

    // 좋아요 추가
    void likeReview(Long reviewId, Long userId);

    // 좋아요 취소
    void unlikeReview(Long reviewId, Long userId);

    // 좋아요 존재 여부 확인
    int countLikes(Long reviewId, Long userId);

    // REVIEW 테이블의 LIKE 수 증가
    void incrementLikeCount(Long reviewId);  // REVIEW 테이블의 LIKE 수 증가

    // REVIEW 테이블의  LIKE 수 감소
    void decrementLikeCount(Long reviewId);  // LIKE 수를 1 감소

    // 새로운 메서드: username으로 리뷰 조회
    List<Review> getReviewsByUsername(String username);

    // 특정 userId와 storeId에 대한 예약 상태 체크 (새로운 메서드)
    int checkReserveStatus(Long userId, Long storeId);

    // 특정 사용자의 매장에 대한 중복 리뷰 확인
    int countReviewsByUserAndStore(Long userId, Long storeId, Long reserveId);

    // 예약 ID를 자동으로 조회
    Long getReserveId(Long userId, Long storeId, String reserveDate);
}

