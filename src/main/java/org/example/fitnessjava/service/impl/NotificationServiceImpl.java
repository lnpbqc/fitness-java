package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.NotificationRepository;
import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.pojo.NotificationType;
import org.example.fitnessjava.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private NotificationRepository notificationRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private CoachRepository coachRepository;

    @Override
    public List<NotificationItem> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<NotificationItem> getNotificationsByReceiver(Integer receiverId, NotificationItem.ReceiverType receiverType) {
        List<NotificationItem> notifications = new ArrayList<>(
                notificationRepository.findByReceiverIdAndReceiverTypeOrderByCreateTimeDesc(receiverId, receiverType)
        );
        if (receiverType == NotificationItem.ReceiverType.CLIENT) {
            notifications.addAll(notificationRepository.findByReceiverIdAndReceiverTypeIsNullOrderByCreateTimeDesc(receiverId));
            notifications.sort(Comparator.comparing(NotificationItem::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        }
        return notifications;
    }

    @Override
    public List<NotificationItem> getUnreadNotificationsByReceiver(Integer receiverId,
                                                                   NotificationItem.ReceiverType receiverType) {
        List<NotificationItem> unreadNotifications = new ArrayList<>(notificationRepository
                .findByReceiverIdAndReceiverTypeAndIsReadOrderByCreateTimeDesc(
                receiverId,
                receiverType,
                false
        ));
        if (receiverType == NotificationItem.ReceiverType.CLIENT) {
            unreadNotifications.addAll(notificationRepository
                    .findByReceiverIdAndReceiverTypeIsNullAndIsReadOrderByCreateTimeDesc(receiverId, false));
            unreadNotifications.sort(Comparator.comparing(NotificationItem::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        }
        return unreadNotifications;
    }

    @Override
    public List<NotificationItem> createNotification(NotificationItem notification) {
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }
        if (notification.getReceiverType() == null) {
            notification.setReceiverType(notification.getReceiverId() == null
                    ? NotificationItem.ReceiverType.ALL
                    : NotificationItem.ReceiverType.CLIENT);
        }
        ArrayList<NotificationItem> notificationItems = new ArrayList<>();
        // 如果是全部的话，应该给每个创建一个记录
        if(notification.getReceiverType().equals(NotificationItem.ReceiverType.ALL)) {
            notification.setReceiverType(NotificationItem.ReceiverType.COACH);
            coachRepository.findAll().forEach(coach -> {
                notification.setReceiverId(coach.getId());
                notificationItems.add(notification);
            });
            notification.setReceiverType(NotificationItem.ReceiverType.CLIENT);
            clientRepository.findAll().forEach(client -> {
                notification.setReceiverId(client.getId());
                notificationItems.add(notification);
            });
        } else {
            notificationItems.add(notification);
        }

        return notificationRepository.saveAll(notificationItems);
    }

    @Override
    public NotificationItem markAsRead(String id) {
        NotificationItem notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Integer receiverId, NotificationItem.ReceiverType receiverType) {
        List<NotificationItem> unreadNotifications = notificationRepository
                .findByReceiverIdAndReceiverTypeAndIsReadOrderByCreateTimeDesc(receiverId, receiverType, false);
        if (receiverType == NotificationItem.ReceiverType.CLIENT) {
            unreadNotifications.addAll(notificationRepository
                    .findByReceiverIdAndReceiverTypeIsNullAndIsReadOrderByCreateTimeDesc(receiverId, false));
        }
        for (NotificationItem notification : unreadNotifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void sendBatchNotifications(List<Integer> receiverIds,
                                       NotificationItem.ReceiverType receiverType,
                                       String title,
                                       String content,
                                       NotificationType type) {
        List<NotificationItem> notifications = new ArrayList<>();

        for (Integer receiverId : receiverIds) {
            NotificationItem notification = new NotificationItem();
            notification.setReceiverId(receiverId);
            notification.setReceiverType(receiverType);
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
