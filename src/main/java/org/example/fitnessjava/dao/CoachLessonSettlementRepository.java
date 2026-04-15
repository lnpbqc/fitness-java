package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.CoachLessonSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachLessonSettlementRepository extends JpaRepository<CoachLessonSettlement, Long> {
    Optional<CoachLessonSettlement> findByBookingId(Integer bookingId);

    List<CoachLessonSettlement> findByCoachId(Integer coachId);

    @Query("SELECT s FROM CoachLessonSettlement s WHERE " +
            "(:startDate IS NULL OR s.settledDate >= :startDate) AND " +
            "(:endDate IS NULL OR s.settledDate <= :endDate) AND " +
            "(:coachId IS NULL OR s.coachId = :coachId) AND " +
            "(:bookingId IS NULL OR s.bookingId = :bookingId) " +
            "ORDER BY s.settledDate DESC, s.id DESC")
    List<CoachLessonSettlement> findForAdmin(@Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("coachId") Integer coachId,
                                             @Param("bookingId") Integer bookingId);
}
