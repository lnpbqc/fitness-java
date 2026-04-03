package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.vo.UserVO;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Boolean addUser(Client user);
    Client existUser(String openid);
    List<UserVO> getAllUsers();
    Optional<UserVO> getUserById(Integer id);
    Optional<UserVO> updateUser(Integer id, String nickname, String avatar, String phone, String role);
    Optional<UserVO> getClientProfileById(Integer id);
    Optional<UserVO> updateClientProfile(Integer id, String memberLevel, Integer points, String membershipExpireAt);
    void convertUserToCoach(Integer userId);
    UserVO convertToUserVO(Client user);
    Client updateClient(Client client);
    Client existUserByUserId(Integer userId);
}
