package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.penddingEntity.Booking;
import org.example.fitnessjava.pojo.penddingEntity.BookingStatus;
import org.example.fitnessjava.pojo.vo.BookingVO;
import org.example.fitnessjava.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/bookings")
@Tag(name = "预约管理", description = "管理后台预约相关接口")
public class AdminBookingController {

    @Resource
    private BookingService bookingService;

    @GetMapping
    @Operation(summary = "获取预约记录列表")
    public ResponseEntity<List<BookingVO>> getBookings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer coachId,
            @RequestParam(required = false) Integer userId) {
        
        List<BookingVO> bookings;
        
        if (status != null || coachId != null || userId != null) {
            BookingStatus bookingStatus = status != null ? BookingStatus.valueOf(status) : null;
            bookings = bookingService.getBookingsWithUserInfo(bookingStatus, coachId, userId);
        } else {
            bookings = bookingService.getAllBookingsWithUserInfo();
        }
        
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取预约详情")
    public ResponseEntity<BookingVO> getBooking(@PathVariable Integer id) {
        BookingVO booking = bookingService.getBookingByIdWithUserInfo(id);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消预约")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        try {
            Booking booking = bookingService.cancelBooking(id, reason != null ? reason : "管理员取消");
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "确认预约")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Integer id) {
        try {
            Booking booking = bookingService.confirmBooking(id);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
