package org.example.fitnessjava;

import jakarta.annotation.Resource;
import org.checkerframework.checker.units.qual.N;
import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.pojo.NotificationType;
import org.example.fitnessjava.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FitnessJavaApplicationTests {

    @Resource
    private NotificationService notificationService;

    @Test
    void contextLoads() {
        NotificationItem notificationItem = new NotificationItem();
        notificationItem.setTitle("12");
        notificationItem.setContent("我是海贼王");
        notificationItem.setReceiverId(null);
        notificationItem.setReceiverType(NotificationItem.ReceiverType.ALL);
        notificationItem.setType(NotificationType.SYSTEM);
        notificationService.createNotification(notificationItem);
        System.out.println(notificationService.getAllNotifications());
    }

}
