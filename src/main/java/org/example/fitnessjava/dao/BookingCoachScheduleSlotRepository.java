package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.BookingCoachScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingCoachScheduleSlotRepository extends JpaRepository<BookingCoachScheduleSlot, Integer> {
    BookingCoachScheduleSlot findByBookingId(int bookingId);

    List<BookingCoachScheduleSlot> findByCoachScheduleSlotId(int coachScheduleSlotId);
}
