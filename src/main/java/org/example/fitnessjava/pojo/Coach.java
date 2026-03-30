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
    private int id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private String intro;
    private String specialty;
    private String description;
    private double rating;
    private int level;
    private int classCount;
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
