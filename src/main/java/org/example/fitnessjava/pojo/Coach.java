package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Coach")
public class Coach {

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
    @Enumerated(EnumType.STRING)
    private Status status;
    public enum Status {
        ONLINE,
        OFFLINE,
        BUSY
    }
}
