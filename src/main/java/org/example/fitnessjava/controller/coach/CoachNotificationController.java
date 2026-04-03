package org.example.fitnessjava.controller.coach;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.service.NotificationService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coach/notifications")
@Slf4j
@Tag(name = "教练端通知管理", description = "教练端通知查询与标记已读接口")
public class CoachNotificationController {

    @Resource
    private NotificationService notificationService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachService coachService;

    @Resource
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "获取通知列表", description = "获取当前教练的通知列表")
    public ResponseEntity<List<NotificationItem>> getNotifications(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        Integer coachId = getCurrentCoachId(token);
        List<NotificationItem> notifications = notificationService.getNotificationsByUserId(coachId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    @Operation(summary = "获取未读通知", description = "获取当前教练的未读通知")
    public ResponseEntity<List<NotificationItem>> getUnreadNotifications(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        Integer coachId = getCurrentCoachId(token);
        List<NotificationItem> unread = notificationService.getUnreadNotificationsByUserId(coachId);
        return ResponseEntity.ok(unread);
    }

    @GetMapping("/unread/count")
    @Operation(summary = "获取未读通知数量", description = "获取当前教练的未读通知数量")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        Integer coachId = getCurrentCoachId(token);
        List<NotificationItem> unread = notificationService.getUnreadNotificationsByUserId(coachId);
        return ResponseEntity.ok(Map.of("count", unread.size()));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "标记通知为已读", description = "标记单条通知为已读")
    public ResponseEntity<NotificationItem> markAsRead(
            @Parameter(description = "通知 ID", required = true)
            @PathVariable String id
    ) {
        NotificationItem notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/read-all")
    @Operation(summary = "全部标记为已读", description = "将当前教练的所有通知标记为已读")
    public ResponseEntity<Map<String, Object>> markAllAsRead(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        Integer coachId = getCurrentCoachId(token);
        notificationService.markAllAsRead(coachId);
        return ResponseEntity.ok(Map.of("success", true, "message", "全部标记已读成功"));
    }

    private Integer getCurrentCoachId(String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new IllegalArgumentException("无效的登录 token");
        }
        Coach coach = coachService.getCoachByOpenid(openid)
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));
        return coach.getId();
    }
}
