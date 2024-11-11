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

    void deleteReview(Long reviewId);

    List<Review> selectReviewsByUserId(Long userId);
}

