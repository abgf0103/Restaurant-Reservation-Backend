package kr.re.kh.model.vo;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationVO {
    private Long reserveId;    // 예약 ID
    private Long userId;           // 사용자 ID
    private Long storeId;          // 가게 ID
    private int partySize;         // 인원 수
    private String reserveStatus;  // 예약 상태
    private LocalDate reserveDate; // 예약 날짜

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;  // CREATED_AT
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;  // UPDATED_AT
}