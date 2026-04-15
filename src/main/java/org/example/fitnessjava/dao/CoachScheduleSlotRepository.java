package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.CoachScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoachScheduleSlotRepository extends JpaRepository<CoachScheduleSlot, Integer> {
    List<CoachScheduleSlot> findAllByOrderByDateAscStartTimeAsc();

    List<CoachScheduleSlot> findAllByDateOrderByStartTimeAsc(String date);

    List<CoachScheduleSlot> findAllByCoachIdOrderByDateAscStartTimeAsc(Integer coachId);


    List<CoachScheduleSlot> findAllByCoachId(Integer coachId);
}
