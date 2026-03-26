package org.example.fitnessjava.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String name;
    private String avatar;
    private String gender;
    private Integer age;

    private String phone;
    private String level;
    private String target;

    private Integer attendance;

    private String nextClass;

    private List<String> tags;

    private String expireDate;
    private Integer sessionsLeft;
    private Integer totalSessions;

    private String joinDate;
    private String packageType;
}
