package kr.re.kh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCleanupService {

    private final NotificationService notificationService;

    // 매일 자정에 실행되도록 설정
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldNotifications() {
        notificationService.deleteOldNotifications();
    }
}
