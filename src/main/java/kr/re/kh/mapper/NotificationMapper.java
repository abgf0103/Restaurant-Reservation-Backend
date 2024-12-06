package kr.re.kh.mapper;

import kr.re.kh.model.vo.NotificationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {

    void insertNotification(NotificationVO notification);

    List<NotificationVO> getAllNotificationsByUserId(Long userId);

    void deleteNotificationById(Long notificationId);

    void deleteAllNotificationsByUserId(Long userId);

    void deleteOldNotifications();
}
