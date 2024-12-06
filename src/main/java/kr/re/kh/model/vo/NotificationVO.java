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
public class NotificationVO {

    private Long notificationId = null;   // 알림 ID
    private Long userId;           // 사용자 ID
    private Long storeId;          // 가게 ID
    private String message;        // 알림 메시지

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;  // 알림 생성 날짜
}
