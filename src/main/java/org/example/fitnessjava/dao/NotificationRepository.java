package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.NotificationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationItem, String> {
    List<NotificationItem> findByReceiverUserId(Integer receiverUserId);
    List<NotificationItem> findByIsRead(Boolean isRead);
    List<NotificationItem> findByReceiverUserIdAndIsRead(Integer receiverUserId, Boolean isRead);
}
