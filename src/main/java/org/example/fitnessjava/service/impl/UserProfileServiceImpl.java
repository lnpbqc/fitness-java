package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ClientProfileRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.CoachWithUserRepository;
import org.example.fitnessjava.dao.UserProfileRepository;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.CoachWithUser;
import org.example.fitnessjava.pojo.UserProfile;
import org.example.fitnessjava.pojo.UserRole;
import org.example.fitnessjava.pojo.ClientProfile;
import org.example.fitnessjava.pojo.vo.UserVO;
import org.example.fitnessjava.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Resource
    private UserProfileRepository userProfileRepository;
    @Resource
    private ClientProfileRepository clientProfileRepository;
    @Resource
    private CoachRepository coachRepository;
    @Resource
    private CoachWithUserRepository coachWithUserRepository;

    @Override
    public Boolean addUser(UserProfile user) {
        UserProfile save = userProfileRepository.save(user);
        Optional<UserProfile> byId = userProfileRepository.findById(Long.valueOf(save.getId()));
        return byId.isPresent();
    }

    @Override
    public UserProfile existUser(String openid) {
        return userProfileRepository.findByOpenid(openid);
    }

    @Override
    public List<UserVO> getAllUsers() {
        List<UserProfile> users = userProfileRepository.findAll();
        return users.stream().map(this::convertToUserVO).collect(Collectors.toList());
    }

    @Override
    public Optional<UserVO> getUserById(Integer id) {
        return userProfileRepository.findById(Long.valueOf(id))
                .map(this::convertToUserVO);
    }

    @Override
    public Optional<UserVO> updateUser(Integer id, String nickname, String avatar, String phone, String role) {
        return userProfileRepository.findById(Long.valueOf(id))
                .map(user -> {
                    if (nickname != null) user.setNickname(nickname);
                    if (avatar != null) user.setAvatar(avatar);
                    if (phone != null) user.setPhone(phone);
                    if (role != null) user.setRole(UserRole.valueOf(role));
                    userProfileRepository.save(user);
                    return convertToUserVO(user);
                });
    }

    @Override
    public List<UserVO> getAllClientProfiles() {
        List<ClientProfile> clients = clientProfileRepository.findAll();
        return clients.stream().map(this::convertToUserVO).collect(Collectors.toList());
    }

    @Override
    public Optional<UserVO> getClientProfileById(Integer id) {
        return clientProfileRepository.findById(Long.valueOf(id))
                .map(this::convertToUserVO);
    }

    @Override
    public Optional<UserVO> updateClientProfile(Integer id, String memberLevel, Integer points, String membershipExpireAt) {
        return clientProfileRepository.findById(Long.valueOf(id))
                .map(client -> {
                    if (memberLevel != null) client.setMemberLevel(memberLevel);
                    if (points != null) client.setPoints(points);
                    if (membershipExpireAt != null) client.setMembershipExpireAt(membershipExpireAt);
                    clientProfileRepository.save(client);
                    return convertToUserVO(client);
                });
    }

    private UserVO convertToUserVO(UserProfile user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setOpenid(user.getOpenid());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setPhone(user.getPhone());
        vo.setRole(user.getRole());

        if (user instanceof ClientProfile) {
            ClientProfile client = (ClientProfile) user;
            vo.setMemberNumber(client.getMemberNumber());
            vo.setMemberLevel(client.getMemberLevel());
            vo.setPoints(client.getPoints());
            vo.setCoupons(client.getCoupons());
            vo.setTotalTrainingCount(client.getTotalTrainingCount());
            vo.setMembershipExpireAt(client.getMembershipExpireAt());
        }

        return vo;
    }

    @Override
    public void convertUserToCoach(Integer userId) {
        UserProfile userProfile = userProfileRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        userProfile.setRole(UserRole.COACH);
        userProfileRepository.save(userProfile);
        
        Coach coach = new Coach();
        coach.setName(userProfile.getNickname());
        coach.setAvatar(userProfile.getAvatar());
        coach.setPhone(userProfile.getPhone());
        coach.setIntro("新加入的教练");
        coach.setSpecialty("待设置");
        coach.setDescription("");
        coach.setRating(5.0);
        coach.setLevel(1);
        coach.setClassCount(0);
        coach.setTags(java.util.Arrays.asList("新教练"));
        coach.setFeatured(false);
        coach.setStatus(Coach.Status.ONLINE);
        coachRepository.save(coach);
        
        CoachWithUser relation = new CoachWithUser();
        relation.setCoachId(coach.getId());
        relation.setUserId(userProfile.getId());
        coachWithUserRepository.save(relation);
    }
}
