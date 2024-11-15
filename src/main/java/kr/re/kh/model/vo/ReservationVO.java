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
    private Long reserveId;   // 예약 ID
    private Long userId;          // 사용자 ID
    private Long storeId;         // 가게 ID
    private int partySize;        // 인원 수
    private String reserveStatus; // 예약 상태

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;  // 생성일시

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;  // 수정일시

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reserveDate;  // 예약일시
}
