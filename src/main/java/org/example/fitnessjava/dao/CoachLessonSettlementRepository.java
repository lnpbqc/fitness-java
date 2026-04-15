package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.CoachLessonSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachLessonSettlementRepository extends JpaRepository<CoachLessonSettlement, Long> {
    Optional<CoachLessonSettlement> findByBookingId(Integer bookingId);

    List<CoachLessonSettlement> findByCoachId(Integer coachId);
}
