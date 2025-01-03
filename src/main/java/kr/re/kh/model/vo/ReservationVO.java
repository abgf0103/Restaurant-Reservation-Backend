package kr.re.kh.model.vo;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationVO {
    private Long reserveId;             // 예약 ID
    private Long userId;                // 예약한 사용자의 ID
    private Long storeId;               // 가게 ID
    private String username;            // 예약자 이름
    private int partySize;              // 인원 수
    private int reserveStatus;          // 예약 상태
    private LocalDateTime reserveDate;  // 예약 날짜
    private String storeName;           // 가게 이름

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;  // CREATED_AT
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;  // UPDATED_AT
}