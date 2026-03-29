package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.UserProfile;
import org.example.fitnessjava.pojo.ClientProfile;
import org.example.fitnessjava.pojo.vo.UserVO;

import java.util.List;
import java.util.Optional;

public interface UserProfileService {
    Boolean addUser(UserProfile user);
    UserProfile existUser(String openid);
    List<UserVO> getAllUsers();
    Optional<UserVO> getUserById(Integer id);
    Optional<UserVO> updateUser(Integer id, String nickname, String avatar, String phone, String role);
    List<UserVO> getAllClientProfiles();
    Optional<UserVO> getClientProfileById(Integer id);
    Optional<UserVO> updateClientProfile(Integer id, String memberLevel, Integer points, String membershipExpireAt);
    void convertUserToCoach(Integer userId);
}
