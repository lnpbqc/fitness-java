package org.example.fitnessjava.repository;

import org.example.fitnessjava.pojo.penddingEntity.Booking;
import org.example.fitnessjava.pojo.penddingEntity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    
    List<Booking> findByStatus(BookingStatus status);
    
    List<Booking> findByCoachId(int coachId);
    
    List<Booking> findByUserId(int userId);
    
    @Query("SELECT b FROM Booking b WHERE " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:coachId IS NULL OR b.coachId = :coachId) AND " +
           "(:userId IS NULL OR b.userId = :userId)")
    List<Booking> findBookings(@Param("status") BookingStatus status,
                               @Param("coachId") Integer coachId,
                               @Param("userId") Integer userId);
}
