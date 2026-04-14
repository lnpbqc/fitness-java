package org.example.fitnessjava.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.NotificationService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client/notifications")
@Slf4j
@Tag(name = "客户端通知接口", description = "客户端通知的查询与标记已读接口")
public class ClientNotificationController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ClientService clientService;

    @Resource
    private NotificationService notificationService;

    @GetMapping
    @Operation(summary = "获取当前用户通知列表", description = "返回当前登录用户的所有通知，按创建时间倒序")
    public List<NotificationItem> getMyNotifications(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentClientId(token);
        return notificationService.getNotificationsByReceiver(userId, NotificationItem.ReceiverType.CLIENT);
    }

    @GetMapping("/unread")
    @Operation(summary = "获取当前用户未读通知", description = "返回当前登录用户的所有未读通知")
    public List<NotificationItem> getUnreadNotifications(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentClientId(token);
        return notificationService.getUnreadNotificationsByReceiver(userId, NotificationItem.ReceiverType.CLIENT);
    }

    @GetMapping("/unread/count")
    @Operation(summary = "获取未读通知数量", description = "返回当前登录用户的未读通知条数")
    public Map<String, Object> getUnreadCount(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentClientId(token);
        List<NotificationItem> unread = notificationService.getUnreadNotificationsByReceiver(
                userId,
                NotificationItem.ReceiverType.CLIENT
        );
        return Map.of("count", unread.size());
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "标记单条通知为已读", description = "将指定通知标记为已读")
    public NotificationItem markAsRead(
            @Parameter(description = "通知ID", example = "xxx-xxx")
            @PathVariable String id
    ) {
        return notificationService.markAsRead(id);
    }

    @PostMapping("/read-all")
    @Operation(summary = "全部标记为已读", description = "将当前用户的所有通知标记为已读")
    public Map<String, Object> markAllAsRead(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentClientId(token);
        notificationService.markAllAsRead(userId, NotificationItem.ReceiverType.CLIENT);
        return Map.of("success", true, "message", "全部标记已读成功");
    }

    private Integer getCurrentClientId(String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户登录信息无效");
        }
        Client client = clientService.existUser(openid);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "当前用户不存在");
        }
        return client.getId();
    }
}