package kr.re.kh.service;

import kr.re.kh.model.vo.NotificationVO;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    void createNotification(NotificationVO notification);

    List<NotificationVO> getAllNotificationsByUserId(Long userId);

    void deleteNotification(Long notificationId);

    void deleteAllNotificationsByUserId(Long userId);

    void deleteOldNotifications();
}
