package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.NotificationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationItem, String> {

    List<NotificationItem> findAllByReceiverIdAndReceiverTypeOrderByCreateTimeDesc(
            Integer receiverId,
            NotificationItem.ReceiverType receiverType
    );

    List<NotificationItem> findByReceiverIdAndReceiverTypeIsNullOrderByCreateTimeDesc(Integer receiverId);

    List<NotificationItem> findByIsRead(Boolean isRead);

    List<NotificationItem> findByReceiverIdAndReceiverTypeAndIsReadOrderByCreateTimeDesc(
            Integer receiverId,
            NotificationItem.ReceiverType receiverType,
            Boolean isRead
    );

    List<NotificationItem> findByReceiverIdAndReceiverTypeIsNullAndIsReadOrderByCreateTimeDesc(
            Integer receiverId,
            Boolean isRead
    );
}
