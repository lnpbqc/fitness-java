package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.BookingStatus;
import org.example.fitnessjava.pojo.CoachScheduleSlot;
import org.example.fitnessjava.pojo.dto.BookingCreateRequest;
import org.example.fitnessjava.pojo.dto.BookingUpdateRequest;
import org.example.fitnessjava.pojo.vo.BookingVO;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    
    List<Booking> getAllBookings();
    
    List<BookingVO> getAllBookingsWithUserInfo();
    
    List<Booking> getBookingsByStatus(BookingStatus status);
    
    List<Booking> getBookingsByCoachId(int coachId);
    
    List<Booking> getBookingsByUserId(int userId);
    
    List<BookingVO> getBookingsWithUserInfo(BookingStatus status, Integer coachId, Integer userId);
    
    Optional<Booking> getBookingById(Integer id);
    
    BookingVO getBookingByIdWithUserInfo(Integer id);
    
    Booking createBooking(Booking booking);
    
    Booking updateBookingStatus(Integer id, BookingStatus status);
    
    Booking cancelBooking(Integer id, String reason);
    
    Booking confirmBooking(Integer id);
    
    void deleteBooking(Integer id);
    
    List<CoachScheduleSlot> getScheduleSlots(Integer coachId, Integer clientId);

    Booking createBooking(Integer userId, BookingCreateRequest request);

    Booking updateBooking(Integer userId, Integer bookingId, BookingUpdateRequest request);

    Booking cancelBooking(Integer userId, Integer bookingId);
}
