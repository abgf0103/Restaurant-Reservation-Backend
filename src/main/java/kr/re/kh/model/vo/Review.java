package kr.re.kh.model.vo;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    private Long reviewId;        // REVIEW_ID
    private Long userId;          // USER_ID
    private Long storeId;         // STORE_ID
    private String storeName;     // 추가된 storeName 필드
    private Double rating;        // RATING
    private String reviewComment; // REVIEW_COMMENT
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;  // CREATED_AT
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;  // UPDATED_AT
    private Integer likeCount;    // LIKE (LIKES)
    private String username;      // USERNAME (사용자 이름)
    private Long reserveId;
    private List<UploadFile> files;   // 파일 정보 리스트 (파일명 등)
    private Long fileId; // 사용자 프로필사진을 조회하기 위한 파일 번호
    private String name;

}
