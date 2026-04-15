package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.dto.CoachProxyBookingRequest;
import org.example.fitnessjava.pojo.dto.CoachRescheduleRequest;
import org.example.fitnessjava.pojo.vo.BookingVO;

import java.util.List;

public interface CoachBookingService {
    List<BookingVO> getBookingsByCoachId(Integer coachId, String date);
    Booking rescheduleBooking(Integer coachId, Integer bookingId, CoachRescheduleRequest request);
    Booking proxyBooking(Integer coachId, CoachProxyBookingRequest request);
    List<Client> getClientsByCoachIdAndSlotId(Integer coachId, Integer slotId);
}
