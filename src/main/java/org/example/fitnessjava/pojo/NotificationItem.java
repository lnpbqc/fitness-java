package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotificationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Integer receiverUserId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String content;

    private Boolean isRead;

    private String actionLink;
    private String createdAt;
}
