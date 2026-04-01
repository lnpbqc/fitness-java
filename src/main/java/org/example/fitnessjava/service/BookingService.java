package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.CoachScheduleSlot;
import org.example.fitnessjava.pojo.dto.BookingCreateRequest;
import org.example.fitnessjava.pojo.dto.BookingUpdateRequest;

import java.util.List;

public interface BookingService {
    List<CoachScheduleSlot> getScheduleSlots(Integer coachId, Integer clientId);

    Booking createBooking(Integer userId, BookingCreateRequest request);

    Booking updateBooking(Integer userId, Integer bookingId, BookingUpdateRequest request);

    Booking cancelBooking(Integer userId, Integer bookingId);
}
