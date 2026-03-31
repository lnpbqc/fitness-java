package org.example.fitnessjava.controller.client;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.dto.BookingCreateRequest;
import org.example.fitnessjava.pojo.dto.BookingUpdateRequest;
import org.example.fitnessjava.pojo.penddingEntity.Booking;
import org.example.fitnessjava.pojo.penddingEntity.CoachScheduleSlot;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.BookingService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client/booking")
@Tag(name = "客户端预约接口", description = "客户端预约与排班查询接口")
public class BookingController {

    @Resource
    private BookingService bookingService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ClientService clientService;

    @GetMapping("/schedule")
    @Operation(summary = "查询排班表", description = "支持根据教练id、客户id查询排班；两个参数都不传时返回全部排班")
    public List<CoachScheduleSlot> getScheduleSlots(
            @Parameter(description = "教练ID，不传则不过滤教练", example = "1")
            @RequestParam(required = false) Integer coachId,
            @Parameter(description = "客户ID，不传则不过滤客户", example = "1")
            @RequestParam(required = false) Integer clientId
    ) {
        return bookingService.getScheduleSlots(coachId, clientId);
    }

    @PostMapping
    @Operation(summary = "新增预约", description = "根据排班时段创建预约记录，并占用排班时段")
    public Booking createBooking(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody BookingCreateRequest request
    ) {
        return bookingService.createBooking(getCurrentClientId(token), request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改预约", description = "支持修改预约时段和地点，修改时会释放原时段并占用新时段")
    public Booking updateBooking(
            @Parameter(description = "预约ID", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody BookingUpdateRequest request
    ) {
        return bookingService.updateBooking(getCurrentClientId(token), id, request);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "取消当前预约，释放排班时段并回退对应课程订单课时")
    public Booking cancelBooking(
            @Parameter(description = "预约ID", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        return bookingService.cancelBooking(getCurrentClientId(token), id);
    }

    private Integer getCurrentClientId(String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new IllegalArgumentException("用户登录信息无效");
        }
        Client client = clientService.existUser(openid);
        if (client == null) {
            throw new IllegalArgumentException("当前用户不存在");
        }
        return client.getId();
    }
}
