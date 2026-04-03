package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

// Class representing a user's profile
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private String memberNumber;
    private String memberLevel;
    private int points;
    private int coupons;
    private int totalTrainingCount;
    private String membershipExpireAt;

    private String gender;
    private Integer age;
    private String joinDate;

    @ElementCollection
    private List<String> tags;
}
