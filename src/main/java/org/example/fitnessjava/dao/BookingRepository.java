package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByUserId(Integer userId);

    Optional<Booking> findByIdAndUserId(Integer id, Integer userId);
    
    List<Booking> findByStatus(BookingStatus status);
    
    List<Booking> findByCoachId(int coachId);
    
    List<Booking> findByUserId(int userId);

    List<Booking> findByBookingDate(String bookingDate);
    
    @Query("SELECT b FROM Booking b WHERE " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:coachId IS NULL OR b.coachId = :coachId) AND " +
           "(:userId IS NULL OR b.userId = :userId)")
    List<Booking> findBookings(@Param("status") BookingStatus status,
                               @Param("coachId") Integer coachId,
                               @Param("userId") Integer userId);
}
