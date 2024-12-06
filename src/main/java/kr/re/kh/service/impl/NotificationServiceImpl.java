package kr.re.kh.service.impl;

import kr.re.kh.mapper.NotificationMapper;
import kr.re.kh.model.vo.NotificationVO;
import kr.re.kh.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    public void createNotification(NotificationVO notification) {
        // createdAt이 비어 있으면 현재 시간으로 설정
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        notificationMapper.insertNotification(notification);
    }

    @Override
    public List<NotificationVO> getAllNotificationsByUserId(Long userId) {
        return notificationMapper.getAllNotificationsByUserId(userId);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationMapper.deleteNotificationById(notificationId);
    }

    @Override
    public void deleteAllNotificationsByUserId(Long userId) {
        notificationMapper.deleteAllNotificationsByUserId(userId);
    }
    @Override
    public void deleteOldNotifications() {
        notificationMapper.deleteOldNotifications();
    }
}
