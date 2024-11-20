package kr.re.kh.service.impl;

import kr.re.kh.exception.BadRequestException;
import kr.re.kh.exception.ResourceNotFoundException;
import kr.re.kh.mapper.FileMapMapper;
import kr.re.kh.mapper.ReviewMapper;
import kr.re.kh.mapper.UploadFileMapper;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.FileDeleteRequest;
import kr.re.kh.model.payload.request.ReviewRequest;
import kr.re.kh.model.vo.Review;
import kr.re.kh.model.vo.SearchHelper;
import kr.re.kh.model.vo.UploadFile;
import kr.re.kh.service.ReviewService;
import kr.re.kh.service.UploadFileService;
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
    private final UploadFileMapper uploadFileMapper;

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
    public HashMap<String, Object> reviewInfo(Long reviewId) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<Review> review = reviewMapper.reviewInfo(reviewId);

        // 리뷰가 존재할 경우 파일 정보 추가
        review.ifPresent(value -> {
            // 리뷰의 reserveId를 기준으로 파일 정보 조회
            SearchHelper s = SearchHelper.builder()
                    .reserveId(value.getReserveId())  // 리뷰의 reserveId 사용
                    .build();

            // 리뷰에 관련된 파일 정보 조회
            List<UploadFile> reviewFiles = reviewMapper.selectReviewsWithFiles(s);
            value.setFiles(reviewFiles);  // 리뷰에 파일 정보 추가
            response.put("data", value);  // 리뷰와 파일 정보 함께 반환
        });

        return response;
    }

    @Override
    public HashMap<String, Object> getReviewsByUsername(String username) {
        HashMap<String, Object> response = new HashMap<>();

        // 리뷰 목록을 username 기준으로 조회
        List<Review> reviews = reviewMapper.getReviewsByUsername(username);

        // 각 리뷰에 대해 파일 정보 조회
        for (Review review : reviews) {
            // 파일 정보 조회를 위한 SearchHelper 생성
            SearchHelper s = SearchHelper.builder()
                    .username(username)  // username 기준으로 파일 정보 조회
                    .reserveId(review.getReserveId())  // 리뷰의 reserveId 사용
                    .build();

            // 리뷰에 대한 파일 정보 조회
            List<UploadFile> reviewFiles = reviewMapper.selectReviewsWithFiles(s);
            review.setFiles(reviewFiles);  // 리뷰에 파일 리스트 추가
        }

        response.put("data", reviews);  // 결과를 'data'로 반환
        return response;
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
    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {
        // 기존 리뷰 정보를 가져옴
        Review review = reviewMapper.reviewInfo(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        // 리뷰 수정
        review.setRating(reviewRequest.getRating());
        review.setReviewComment(reviewRequest.getReviewComment());
        review.setStoreId(reviewRequest.getStoreId());

        // 리뷰 업데이트
        reviewMapper.updateReview(review);

        // 파일 수정 로직 추가
        if (reviewRequest.getFiles() != null && !reviewRequest.getFiles().isEmpty()) {
            // 파일맵 저장
            List<HashMap<String, Object>> fileListMap = new ArrayList<>();
            for (Long fileId : reviewRequest.getFiles()) {
                HashMap<String, Object> fileMap = new HashMap<>();
                fileMap.put("userId", review.getUserId());
                fileMap.put("storeId", review.getStoreId());
                fileMap.put("reserveId", review.getReserveId());
                fileMap.put("fileId", fileId);
                reviewMapper.insertFileMap(fileMap);
                fileListMap.add(fileMap);
            }
        }
    }

    @Override
    public void deleteReview(Long reviewId, Long reserveId, Long userId) {
        // 로그로 받은 값 확인
        log.info("리뷰 삭제 요청 - reviewId: {}, reserveId: {}, userId: {} ", reviewId, reserveId, userId);

        // 1. REVIEW_LIKE 테이블에서 해당 리뷰의 좋아요 데이터 삭제
        reviewMapper.deleteReviewLikes(reviewId);

        // 2. REVIEW_FILE_MAP에서 삭제될 FILE_ID값을, 해당 FILE_ID에 대응되는 UPLOAD_FILE 테이블의 ID값에다가  설정
        List<Long> fileIdsToDelete = reviewMapper.getFileIdsByReserveId(reserveId); // reserveId에 해당하는 FILE_ID 목록을 가져옵니다.

        log.info("삭제할 FILE_ID 목록: {}", fileIdsToDelete);

        // 3. REVIEW_FILE_MAP 테이블에서 관련된 파일 삭제
        reviewMapper.deleteReviewFiles(reserveId); // 리뷰와 관련된 파일 삭제

        // 4. REVIEW 테이블에서 해당 리뷰 삭제
        reviewMapper.deleteReview(reviewId);

        // 5. fileIdsToDelete에 포함된 FILE_ID 값들을 UPLOAD_FILE 테이블에서 삭제
        for (Long fileId : fileIdsToDelete) {
            // 해당 FILE_ID로 UPLOAD_FILE 테이블에서 파일 삭제
            UploadFile fileToDelete = new UploadFile();
            fileToDelete.setId(fileId); // 삭제할 파일 ID 설정

            // 파일 삭제 메서드 호출
            uploadFileMapper.deleteByFileByIdAndFileTarget(fileToDelete); // 해당 파일 삭제
        }

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
