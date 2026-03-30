package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Class representing a user's profile
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {

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
}
