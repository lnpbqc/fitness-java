package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.CoachScheduleSlotRepository;
import org.example.fitnessjava.dao.CoachWithUserRepository;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.CoachWithUser;
import org.example.fitnessjava.pojo.CoachScheduleSlot;
import org.example.fitnessjava.service.CoachService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CoachServiceImpl implements CoachService {
    @Resource
    private CoachRepository coachRepository;
    @Resource
    private ClientRepository clientRepository;
    @Resource
    private CoachWithUserRepository coachWithUserRepository;
    @Resource
    private CoachScheduleSlotRepository coachScheduleSlotRepository;

    @Override
    public ArrayList<Coach> getCoachesByFeatured() {
        List<Coach> byFeatured = coachRepository.findByFeatured(true);
        return new ArrayList<>(byFeatured);
    }

    @Override
    public ArrayList<Coach> getAllCoaches() {
        List<Coach> all = coachRepository.findAll();
        return new ArrayList<>(all);
    }

    @Override
    public ArrayList<Coach> getAllVisibleCoaches() {
        return new ArrayList<>(coachRepository.findByVerified(true));
    }

    @Override
    public Coach createCoach(Coach coach) {
        if (coach.getRating() == null || coach.getRating() == 0) {
            coach.setRating(5.0);
        }
        if (coach.getClassCount() == null || coach.getClassCount() == 0) {
            coach.setClassCount(0);
        }
        if (coach.getFeatured() == null) {
            coach.setFeatured(false);
        }
        if (coach.getStatus() == null) {
            coach.setStatus(Coach.Status.ONLINE);
        }
        if (coach.getVerified() == null) {
            coach.setVerified(false);
        }
        if (coach.getTags() == null) {
            coach.setTags(new java.util.ArrayList<>());
        }
        if (coach.getLevel() == null) {
            coach.setLevel(1);
        }
        return coachRepository.save(coach);
    }

    @Override
    public Optional<Coach> getCoachById(Long id) {
        return coachRepository.findById(id);
    }

    @Override
    public Optional<Coach> updateCoach(Long id, String name, String avatar, String intro, String specialty, String description, Double rating, Integer level, String phone, Coach.Status status, Boolean featured) {
        Optional<Coach> optional = coachRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Coach coach = optional.get();
        if (name != null) coach.setNickname(name);
        if (avatar != null) coach.setAvatar(avatar);
        if (intro != null) coach.setIntro(intro);
        if (specialty != null) coach.setSpecialty(specialty);
        if (description != null) coach.setDescription(description);
        if (rating != null) coach.setRating(rating);
        if (level != null) coach.setLevel(level);
        if (phone != null) coach.setPhone(phone);
        if (status != null) coach.setStatus(status);
        if (featured != null) coach.setFeatured(featured);
        return Optional.of(coachRepository.save(coach));
    }

    @Override
    public Optional<Coach> updateCoachFeatured(Long id, Boolean featured) {
        Optional<Coach> optional = coachRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Coach coach = optional.get();
        coach.setFeatured(featured);
        return Optional.of(coachRepository.save(coach));
    }

    @Override
    public Optional<Coach> updateCoachStatus(Long id, Coach.Status status) {
        Optional<Coach> optional = coachRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Coach coach = optional.get();
        coach.setStatus(status);
        return Optional.of(coachRepository.save(coach));
    }

    @Override
    public void deleteCoach(Long id) {
        coachRepository.deleteById(id);
    }

    @Override
    public ArrayList<Coach> getTodayCoaches() {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<CoachScheduleSlot> todaySlots = coachScheduleSlotRepository.findAllByDateOrderByStartTimeAsc(today);
        if (todaySlots.isEmpty()) {
            return getFallbackTodayCoaches();
        }

        ArrayList<Coach> todayCoaches = new ArrayList<>();
        Set<Integer> addedCoachIds = new LinkedHashSet<>();
        // 按当天排班时段顺序去重返回教练列表。
        for (CoachScheduleSlot todaySlot : todaySlots) {
            if (!addedCoachIds.add(todaySlot.getCoachId())) {
                continue;
            }
            Optional<Coach> coachOptional = coachRepository.findById((long) todaySlot.getCoachId());
            if (coachOptional.isEmpty()) {
                continue;
            }
            Coach coach = coachOptional.get();
            if (coach.getStatus() == null || coach.getStatus() != Coach.Status.OFFLINE) {
                todayCoaches.add(coach);
            }
        }
        return todayCoaches;
    }

    private ArrayList<Coach> getFallbackTodayCoaches() {
        ArrayList<Coach> todayCoaches = new ArrayList<>();
        List<Coach> allCoaches = coachRepository.findAll();
        // 当天尚未配置排班时，降级为返回非离线教练，避免列表直接为空。
        for (Coach coach : allCoaches) {
            if (coach.getStatus() == null || coach.getStatus() != Coach.Status.OFFLINE) {
                todayCoaches.add(coach);
            }
        }
        return todayCoaches;
    }

    @Override
    public ArrayList<Coach> getCoachesOfUser(String openid) {
        ArrayList<Coach> coaches = new ArrayList<>();
        Client byId = clientRepository.findByOpenid(openid);
        if (byId==null) {
            return coaches;
        }
        ArrayList<CoachWithUser> allByClientId = coachWithUserRepository.findAllByClientId(byId.getId());
        for (CoachWithUser coachWithUser : allByClientId) {
            Optional<Coach> coach = coachRepository.findById((long) coachWithUser.getCoachId());
            coach.ifPresent(coaches::add);
        }
        return coaches;
    }

    @Override
    public Optional<Coach> getCoachByOpenid(String openid) {
        return coachRepository.findByOpenid(openid);
    }

    @Override
    public ArrayList<Client> getClientsByCoachId(int coachId) {
        ArrayList<Client> clients = new ArrayList<>();
        ArrayList<CoachWithUser> relations = coachWithUserRepository.findAllByCoachId(coachId);
        for (CoachWithUser relation : relations) {
            Optional<Client> client = clientRepository.findById((long) relation.getClientId());
            client.ifPresent(clients::add);
        }
        return clients;
    }

    @Override
    public ArrayList<Coach> getUnverifiedCoaches() {
        List<Coach> list = coachRepository.findByVerified(false);
        return new ArrayList<>(list);
    }

    @Override
    public Optional<Coach> updateCoachVerified(Long id, Boolean verified) {
        Optional<Coach> optional = coachRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Coach coach = optional.get();
        coach.setVerified(verified);
        return Optional.of(coachRepository.save(coach));
    }
}
