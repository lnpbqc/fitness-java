package org.example.fitnessjava.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class NotificationItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer receiverId;

    @Enumerated(EnumType.STRING)
    private ReceiverType receiverType;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String content;

    private Boolean isRead;

    private String actionLink;

    public enum ReceiverType {
        CLIENT,
        COACH,
        ALL
    }
}
