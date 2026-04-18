package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.pojo.NotificationType;

import java.util.List;

public interface NotificationService {

    List<NotificationItem> getAllNotifications();

    List<NotificationItem> getNotificationsByReceiver(Integer receiverId, NotificationItem.ReceiverType receiverType);

    List<NotificationItem> getUnreadNotificationsByReceiver(Integer receiverId, NotificationItem.ReceiverType receiverType);

    List<NotificationItem> createNotification(NotificationItem notification);

    NotificationItem markAsRead(String id);

    void markAllAsRead(Integer receiverId, NotificationItem.ReceiverType receiverType);

    void sendBatchNotifications(List<Integer> receiverIds,
                                NotificationItem.ReceiverType receiverType,
                                String title,
                                String content,
                                NotificationType type);

    void deleteNotification(String id);
}
