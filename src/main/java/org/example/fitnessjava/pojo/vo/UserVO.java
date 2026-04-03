package org.example.fitnessjava.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Integer id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private String memberNumber;
    private String memberLevel;
    private Integer points;
    private Integer coupons;
    private Integer totalTrainingCount;
    private String membershipExpireAt;
    private String gender;
    private Integer age;
    private String joinDate;
    private List<String> tags;
}
