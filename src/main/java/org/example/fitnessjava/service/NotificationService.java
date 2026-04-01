package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.pojo.NotificationType;

import java.util.ArrayList;
import java.util.List;

public interface NotificationService {
    List<NotificationItem> getAllNotifications();
    List<NotificationItem> getNotificationsByUserId(Integer userId);
    List<NotificationItem> getUnreadNotifications();
    List<NotificationItem> getUnreadNotificationsByUserId(Integer userId);
    NotificationItem createNotification(NotificationItem notification);
    NotificationItem markAsRead(String id);
    void markAllAsRead(Integer userId);
    void sendBatchNotifications(List<Integer> userIds, String title, String content, NotificationType type);
    void deleteNotification(String id);
}
