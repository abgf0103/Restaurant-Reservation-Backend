package kr.re.kh.mapper;

import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;
import kr.re.kh.model.vo.UploadFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewMapper {

    List<Review> selectReview(SearchHelper searchHelper);

    // 리뷰와 파일 정보를 함께 조회하는 새로운 메서드
    List<UploadFile> selectReviewsWithFiles(SearchHelper searchHelper);

    List<Review> selectReviewsByUserId(Long userId);

    void reviewSave(Review review);  // REVIEW에 USERNAME 포함

    void insertFileMap(HashMap<String, Object> map);

    Optional<Review> reviewInfo(Long reviewId);

    void updateReview(Review review);

    // 리뷰 좋아요 삭제
    void deleteReviewLikes(Long reviewId);

    // 리뷰 삭제
    void deleteReview(Long reviewId);

    // 리뷰 첨부파일 삭제
    void deleteReviewFiles(Long reserveId);

    // REVIEW_FILE_MAP에서 삭제된 FILE_ID를 기준으로, 해당 FILE_ID에 UPLOAD_FILE 테이블의 ID값 가져오기
    List<Long> getFileIdsByReserveId(Long reserveId);

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
    int checkReserveStatus(Long userId, Long storeId, Long reserveId);

    // 특정 사용자의 매장에 대한 중복 리뷰 확인
    int countReviewsByUserAndStore(Long userId, Long storeId, Long reserveId);

    double getRatingAvgByStoreId(Long storeId); //가게ID로 리뷰 평균 별점 조회

    Long getReviewCountByStoreId(Long storeId); //가게ID로 리뷰 개수 조회

    void reviewLikeDeleteForAdmin(Long reviewId); //리뷰ID로 리뷰 삭제(어드민)
}


