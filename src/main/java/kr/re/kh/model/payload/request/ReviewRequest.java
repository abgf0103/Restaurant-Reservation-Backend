package kr.re.kh.model.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewRequest {

    private Long userId;          // USER_ID
    private Long storeId;         // STORE_ID
    private Double rating;        // RATING
    private String reviewComment; // REVIEW_COMMENT
    private Long reserveId;  // RESERVE_ID

}
