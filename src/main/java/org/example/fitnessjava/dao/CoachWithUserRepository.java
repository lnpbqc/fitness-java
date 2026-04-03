package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.CoachWithUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CoachWithUserRepository extends JpaRepository<CoachWithUser, Long> {
    Optional<CoachWithUser> findByClientId(Integer clientId);
    Optional<CoachWithUser> findByCoachId(Integer coachId);

    ArrayList<CoachWithUser> findAllByClientId(int clientId);

    ArrayList<CoachWithUser> findAllByCoachId(int coachId);

    boolean existsByCoachIdAndClientId(int coachId, int clientId);
}
