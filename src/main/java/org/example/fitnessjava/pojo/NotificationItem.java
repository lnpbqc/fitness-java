package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
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
    private ReceiverType receiverType;  // 根据这个type来确定是哪个表的id

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
