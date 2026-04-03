package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.CoachWithUserRepository;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.CoachWithUser;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.vo.UserVO;
import org.example.fitnessjava.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    @Resource
    private ClientRepository clientRepository;
    @Resource
    private CoachRepository coachRepository;
    @Resource
    private CoachWithUserRepository coachWithUserRepository;

    @Override
    public Boolean addUser(Client user) {
        Client save = clientRepository.save(user);
        Optional<Client> byId = clientRepository.findById(Long.valueOf(save.getId()));
        return byId.isPresent();
    }

    @Override
    public Client existUser(String openid) {
        return clientRepository.findByOpenid(openid);
    }

    @Override
    public List<UserVO> getAllUsers() {
        List<Client> users = clientRepository.findAll();
        return users.stream().map(this::convertToUserVO).collect(Collectors.toList());
    }

    @Override
    public Optional<UserVO> getUserById(Integer id) {
        return clientRepository.findById(Long.valueOf(id))
                .map(this::convertToUserVO);
    }

    @Override
    public Optional<UserVO> updateUser(Integer id, String nickname, String avatar, String phone, String role) {
        return clientRepository.findById(Long.valueOf(id))
                .map(user -> {
                    if (nickname != null) user.setNickname(nickname);
                    if (avatar != null) user.setAvatar(avatar);
                    if (phone != null) user.setPhone(phone);
                    clientRepository.save(user);
                    return convertToUserVO(user);
                });
    }

    @Override
    public Optional<UserVO> getClientProfileById(Integer id) {
        return clientRepository.findById(Long.valueOf(id))
                .map(this::convertToUserVO);
    }

    @Override
    public Optional<UserVO> updateClientProfile(Integer id, String memberLevel, Integer points, String membershipExpireAt) {
        return clientRepository.findById(Long.valueOf(id))
                .map(client -> {
                    if (memberLevel != null) client.setMemberLevel(memberLevel);
                    if (points != null) client.setPoints(points);
                    if (membershipExpireAt != null) client.setMembershipExpireAt(membershipExpireAt);
                    clientRepository.save(client);
                    return convertToUserVO(client);
                });
    }

    public UserVO convertToUserVO(Client user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setOpenid(user.getOpenid());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setPhone(user.getPhone());

        vo.setMemberNumber(user.getMemberNumber());
        vo.setMemberLevel(user.getMemberLevel());
        vo.setPoints(user.getPoints());
        vo.setCoupons(user.getCoupons());
        vo.setTotalTrainingCount(user.getTotalTrainingCount());
        vo.setMembershipExpireAt(user.getMembershipExpireAt());
        vo.setGender(user.getGender());
        vo.setAge(user.getAge());
        vo.setJoinDate(user.getJoinDate());
        vo.setTags(user.getTags());

        return vo;
    }

    @Override
    public void convertUserToCoach(Integer userId) {
        Client client = clientRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        clientRepository.save(client);
        
        Coach coach = new Coach();
        coach.setNickname(client.getNickname());
        coach.setAvatar(client.getAvatar());
        coach.setPhone(client.getPhone());
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
        relation.setClientId(client.getId());
        coachWithUserRepository.save(relation);
    }

    @Override
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client existUserByUserId(Integer userId) {
        return clientRepository.findById(Long.valueOf(userId)).orElse(null);
    }
}
