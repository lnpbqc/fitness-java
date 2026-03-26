package org.example.fitnessjava.pojo;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ClientProfile")
public class ClientProfile extends UserProfile {
    private String memberNumber;
    private String memberLevel;
    private int points;
    private int coupons;
    private int totalTrainingCount;
    private String membershipExpireAt;
}
