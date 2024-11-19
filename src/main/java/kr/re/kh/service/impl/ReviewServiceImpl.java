package kr.re.kh.service.impl;

import kr.re.kh.exception.BadRequestException;
import kr.re.kh.exception.ResourceNotFoundException;
import kr.re.kh.mapper.ReviewMapper;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.ReviewRequest;
import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;
import kr.re.kh.model.vo.UploadFile;
import kr.re.kh.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        // 기존 리뷰 목록 조회 (기본 리뷰 정보)
        List<Review> reviews = reviewMapper.selectReview(searchHelper);

        for (Review item : reviews) {
            // 리뷰와 파일 목록 조회
            SearchHelper s = SearchHelper.builder()
                    .userId(item.getUserId())
                    .storeId(item.getStoreId())
                    .reserveId(item.getReserveId())
                    .build();
            List<UploadFile> reviewsWithFiles = reviewMapper.selectReviewsWithFiles(s);
            item.setFiles(reviewsWithFiles);
        }
        response.put("data", reviews);

        return response;
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        // 내가 작성한 리뷰 목록 조회
        List<Review> reviews = reviewMapper.selectReviewsByUserId(userId);

        // 각 리뷰에 대한 좋아요 수와 파일 정보를 설정
        for (Review review : reviews) {
            // 좋아요 수 설정
            int likeCount = reviewMapper.countLikes(review.getReviewId(), userId); // 현재 사용자가 좋아요를 눌렀는지 체크
            review.setLikeCount(likeCount); // Review 객체에 likeCount 설정

            // 리뷰에 대한 파일 정보 조회
            SearchHelper s = SearchHelper.builder()
                    .userId(userId)
                    .reserveId(review.getReserveId())  // 리뷰의 reserveId 사용
                    .build();
            List<UploadFile> reviewFiles = reviewMapper.selectReviewsWithFiles(s);
            review.setFiles(reviewFiles);  // 리뷰에 파일 리스트 추가
        }

        return reviews;
    }



    @Override
    public List<HashMap<String, Object>> saveReview(CustomUserDetails currentUser, ReviewRequest reviewRequest) {
        Long userID = currentUser.getId();
        String username = currentUser.getUsername();

        // 클라이언트로부터 전달된 reserveId를 그대로 사용
        Long reserveId = reviewRequest.getReserveId();

        // 리뷰 객체 생성
        Review review = Review.builder()
                .userId(userID)
                .storeId(reviewRequest.getStoreId())
                .rating(reviewRequest.getRating())
                .reviewComment(reviewRequest.getReviewComment())
                .username(username)
                .reserveId(reserveId)  // 클라이언트로부터 전달받은 reserveId 추가
                .build();

        // 로그 출력
        log.info(reviewRequest.toString());
        log.info(review.toString());

        // 리뷰 저장
        reviewMapper.reviewSave(review);

        // 파일맵 저장
        List<HashMap<String, Object>> fileListMap = new ArrayList<>();
        for (Long fileId : reviewRequest.getFiles()) {
            HashMap<String, Object> fileMap = new HashMap<>();
            fileMap.put("userId", currentUser.getId());
            fileMap.put("storeId", reviewRequest.getStoreId());
            fileMap.put("reserveId", reviewRequest.getReserveId());
            fileMap.put("fileId", fileId);
            reviewMapper.insertFileMap(fileMap);
            fileListMap.add(fileMap);
        }
        return fileListMap;
    }

    @Override
    public HashMap<String, Object> reviewInfo(Long reviewId) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<Review> review = reviewMapper.reviewInfo(reviewId);
        review.ifPresent(value -> response.put("data", value));
        return response;
    }

    @Override
    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {
        Review review = reviewMapper.reviewInfo(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        review.setRating(reviewRequest.getRating());
        review.setReviewComment(reviewRequest.getReviewComment());
        review.setStoreId(reviewRequest.getStoreId());

        reviewMapper.updateReview(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long reserveId, Long userId ) {
        // 로그로 받은 값 확인
        log.info("리뷰 삭제 요청 - reviewId: {}, reserveId: {}, userId: {} ", reviewId, reserveId, userId);

        // 1. REVIEW_LIKE 테이블에서 해당 리뷰의 좋아요 데이터 삭제
        reviewMapper.deleteReviewLikes(reviewId);

        // 2. REVIEW 테이블에서 해당 리뷰 삭제
        reviewMapper.deleteReview(reviewId);

        // 3. REVIEW_FILE_MAP 테이블에서 관련된 파일 삭제
        reviewMapper.deleteReviewFiles(reserveId);
        log.info("리뷰 삭제 완료, reserveId({})를 기준으로 관련 파일 삭제됨", reserveId);
    }

    @Override
    public boolean likeReview(Long reviewId, Long userId) {
        // 이미 좋아요를 눌렀는지 체크
        int count = reviewMapper.countLikes(reviewId, userId);
        if (count == 0) {
            // 좋아요 추가
            reviewMapper.likeReview(reviewId, userId);
            // REVIEW 테이블의 LIKE 컬럼 업데이트
            reviewMapper.incrementLikeCount(reviewId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unlikeReview(Long reviewId, Long userId) {
        // 좋아요 취소
        reviewMapper.unlikeReview(reviewId, userId);
        // REVIEW 테이블의 LIKE 수 감소
        reviewMapper.decrementLikeCount(reviewId);
    }

    @Override
    public boolean isReviewLikedByUser(Long reviewId, Long userId) {
        int count = reviewMapper.countLikes(reviewId, userId);
        return count > 0;
    }

    // 새로운 메서드: username으로 리뷰 조회
    @Override
    public HashMap<String, Object> getReviewsByUsername(String username) {
        HashMap<String, Object> response = new HashMap<>();
        List<Review> reviews = reviewMapper.getReviewsByUsername(username); // Mapper 호출
        response.put("data", reviews); // 리뷰 목록을 'data'로 반환
        return response;
    }

    @Override
    public boolean reserveStatusCheck(Long userId, Long storeId, Long reserveId) {
        // 예약 상태를 조회할 Mapper 호출
        int count = reviewMapper.checkReserveStatus(userId, storeId, reserveId);
        return count > 0;  // 예약이 있고, 상태가 2인 경우 리뷰 작성 가능
    }

    @Override
    public boolean isReviewExist(Long userId, Long storeId, Long reserveId) {
        // 예약이 존재하고 해당 예약에 이미 리뷰가 작성된 경우
        int count = reviewMapper.countReviewsByUserAndStore(userId, storeId, reserveId);
        return count > 0;
    }
}
