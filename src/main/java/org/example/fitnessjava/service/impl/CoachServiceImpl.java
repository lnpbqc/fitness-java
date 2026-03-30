package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.service.CoachService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoachServiceImpl implements CoachService {
    @Resource
    private CoachRepository coachRepository;

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
    public Coach createCoach(Coach coach) {
        if (coach.getRating() == 0) {
            coach.setRating(5.0);
        }
        if (coach.getClassCount() == 0) {
            coach.setClassCount(0);
        }
        if (coach.getFeatured() == null) {
            coach.setFeatured(false);
        }
        if (coach.getStatus() == null) {
            coach.setStatus(Coach.Status.ONLINE);
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
}
