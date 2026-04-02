package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.NotificationRepository;
import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.pojo.NotificationType;
import org.example.fitnessjava.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private NotificationRepository notificationRepository;

    @Override
    public List<NotificationItem> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<NotificationItem> getNotificationsByUserId(Integer userId) {
        return notificationRepository.findByReceiverUserId(userId);
    }

    @Override
    public List<NotificationItem> getUnreadNotifications() {
        return notificationRepository.findByIsRead(false);
    }

    @Override
    public List<NotificationItem> getUnreadNotificationsByUserId(Integer userId) {
        return notificationRepository.findByReceiverUserIdAndIsRead(userId, false);
    }

    @Override
    public NotificationItem createNotification(NotificationItem notification) {
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }
        return notificationRepository.save(notification);
    }

    @Override
    public NotificationItem markAsRead(String id) {
        NotificationItem notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Integer userId) {
        List<NotificationItem> unreadNotifications = notificationRepository.findByReceiverUserIdAndIsRead(userId, false);
        for (NotificationItem notification : unreadNotifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void sendBatchNotifications(List<Integer> userIds, String title, String content, NotificationType type) {
        List<NotificationItem> notifications = new ArrayList<>();
        
        for (Integer userId : userIds) {
            NotificationItem notification = new NotificationItem();
            notification.setReceiverUserId(userId);
            notification.setType(type);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setIsRead(false);
            notifications.add(notification);
        }
        
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteNotification(String id) {
        notificationRepository.deleteById(id);
    }
}
