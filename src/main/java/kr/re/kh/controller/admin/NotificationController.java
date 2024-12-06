package kr.re.kh.controller.admin;

import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.response.ApiResponse;
import kr.re.kh.model.vo.NotificationVO;
import kr.re.kh.service.NotificationService;
import kr.re.kh.service.ReservationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final ReservationService reservationService;

    /**
     * 알림 생성
     * @param notification
     */
    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody NotificationVO notification) {
        notificationService.createNotification(notification);
        return ResponseEntity.ok("알림이 생성되었습니다.");
    }
    /**
     * 특정 사용자의 모든 알림 조회
     * @param currentUser
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<List<NotificationVO>> getAllNotificationsByUserId(@CurrentUser CustomUserDetails currentUser) {
        List<NotificationVO> notifications = notificationService.getAllNotificationsByUserId(currentUser.getId());
        log.info(notifications.toString());
        return ResponseEntity.ok(notifications);
    }

    /**
     * 특정 알림 삭제
     * @param notificationId
     */
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("알림이 삭제되었습니다.");
    }

    /**
     * 특정 사용자의 모든 알림 삭제
     * @param userId
     */
    @DeleteMapping("/user/delete/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<String> deleteAllNotificationsByUserId(@PathVariable Long userId) {
        notificationService.deleteAllNotificationsByUserId(userId);
        return ResponseEntity.ok("사용자의 모든 알림이 삭제되었습니다.");
    }

    /**
     * 예약 상태 업데이트 (가게 회원 전용) 후 알림 발송
     * @param reserveId
     * @param statusMap
     * @param currentUser
     * @return
     */
    @PutMapping("/update-status/{reserveId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> updateReservationStatus(@PathVariable Long reserveId,
                                                     @RequestBody HashMap<String, Object> statusMap,
                                                     @CurrentUser CustomUserDetails currentUser) {
        int status = (int) statusMap.get("status");

        // 예약 상태 업데이트
        reservationService.updateReservationStatus(reserveId, status, currentUser.getId());

        return ResponseEntity.ok(new ApiResponse(true, "예약 상태가 업데이트되었고, 알림이 전송되었습니다."));
    }

}
