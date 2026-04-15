package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.CoachWithUser;

import java.util.ArrayList;
import java.util.Optional;

public interface CoachService {
    ArrayList<Coach> getCoachesByFeatured();

    ArrayList<Coach> getAllCoaches();

    ArrayList<Coach> getAllVisibleCoaches();

    Optional<Coach> getCoachById(Long id);

    Coach createCoach(Coach coach);

    Optional<Coach> updateCoach(Long id, String name, String avatar, String intro, String specialty, String description, Double rating, Integer level, String phone, Coach.Status status, Boolean featured);

    Optional<Coach> updateCoachFeatured(Long id, Boolean featured);

    Optional<Coach> updateCoachStatus(Long id, Coach.Status status);

    void deleteCoach(Long id);

    ArrayList<Coach> getTodayCoaches();

    ArrayList<Coach> getCoachesOfUser(String openid);

    ArrayList<Client> getClientsByCoachId(int coachId);

    Optional<Coach> getCoachByOpenid(String openid);

    ArrayList<Coach> getUnverifiedCoaches();

    Optional<Coach> updateCoachVerified(Long id, Boolean verified);

    Optional<CoachWithUser> addCoachWithUser(Integer coachId, String qrCode);
}