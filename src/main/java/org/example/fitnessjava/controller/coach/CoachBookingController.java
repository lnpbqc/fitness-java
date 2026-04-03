package org.example.fitnessjava.controller.coach;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.dto.CoachProxyBookingRequest;
import org.example.fitnessjava.pojo.dto.CoachRescheduleRequest;
import org.example.fitnessjava.pojo.vo.BookingVO;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.CoachBookingService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach/bookings")
@Slf4j
@Tag(name = "教练端预约管理", description = "教练端预约查询、改期、代约接口")
public class CoachBookingController {

    @Resource
    private CoachBookingService coachBookingService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachService coachService;

    @Resource
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "获取教练预约列表", description = "获取当前教练的预约列表，支持按日期过滤")
    public ResponseEntity<List<BookingVO>> getBookings(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "日期 YYYY-MM-DD，不传返回全部")
            @RequestParam(required = false) String date
    ) {
        Coach coach = getCurrentCoach(token);
        List<BookingVO> bookings = coachBookingService.getBookingsByCoachId(coach.getId(), date);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{bookingId}/reschedule")
    @Operation(summary = "教练改期", description = "教练为指定预约申请改期，提交后需学员确认")
    public ResponseEntity<Booking> rescheduleBooking(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "预约 ID", required = true)
            @PathVariable Integer bookingId,
            @RequestBody CoachRescheduleRequest request
    ) {
        Coach coach = getCurrentCoach(token);
        Booking result = coachBookingService.rescheduleBooking(coach.getId(), bookingId, request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/proxy")
    @Operation(summary = "教练代客预约", description = "教练为指定会员代约课程")
    public ResponseEntity<Booking> proxyBooking(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody CoachProxyBookingRequest request
    ) {
        Coach coach = getCurrentCoach(token);
        if (request.getCoachId() == null) {
            request.setCoachId(coach.getId());
        }
        Booking result = coachBookingService.proxyBooking(coach.getId(), request);
        return ResponseEntity.ok(result);
    }

    private Coach getCurrentCoach(String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new IllegalArgumentException("无效的登录 token");
        }
        Coach coach = coachService.getCoachByOpenid(openid)
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));
        return coach;
    }
}
