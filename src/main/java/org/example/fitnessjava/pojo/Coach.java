package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Coach")
@EqualsAndHashCode(callSuper = true)
public class Coach extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private String intro;
    private String specialty;
    private String description;
    private Double rating;
    private Integer level;
    private Integer classCount;
    private java.util.List<String> tags;
    private Boolean featured;
    private Boolean verified;
    @Enumerated(EnumType.STRING)
    private Status status;
    public enum Status {
        ONLINE,
        OFFLINE,
        BUSY
    }
}
