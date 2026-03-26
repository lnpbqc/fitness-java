package org.example.fitnessjava.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Class representing a user's profile
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "UserProfile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nickname;
    private String avatar;
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
