package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.NotificationItem;
import org.example.fitnessjava.pojo.NotificationType;
import org.example.fitnessjava.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notifications")
@Tag(name = "通知管理", description = "管理后台通知相关接口")
public class AdminNotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping
    @Operation(summary = "获取所有通知列表")
    public ResponseEntity<List<NotificationItem>> getNotifications() {
        List<NotificationItem> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取通知详情")
    public ResponseEntity<NotificationItem> getNotification(@PathVariable String id) {
        return notificationService.getAllNotifications().stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建系统通知")
    public ResponseEntity<?> createNotification(@RequestBody NotificationItem request) {
        try {
            if (request.getTitle() == null || request.getTitle().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "标题不能为空"));
            }
            if (request.getContent() == null || request.getContent().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "内容不能为空"));
            }

            NotificationItem notification = notificationService.createNotification(request);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "创建失败：" + e.getMessage()));
        }
    }

    @PostMapping("/batch")
    @Operation(summary = "批量推送通知")
    public ResponseEntity<Map<String, Object>> sendBatchNotifications(
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> userIds = (List<Integer>) request.get("userIds");
            String title = (String) request.get("title");
            String content = (String) request.get("content");
            String typeStr = (String) request.get("type");
            String receiverTypeStr = (String) request.get("receiverType");

            if (userIds == null || userIds.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "用户 ID 列表不能为空"
                ));
            }
            if (title == null || title.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "标题不能为空"
                ));
            }
            if (content == null || content.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "内容不能为空"
                ));
            }

            if (typeStr == null || typeStr.isEmpty()) {
                typeStr = "SYSTEM";
            }
            if (receiverTypeStr == null || receiverTypeStr.isEmpty()) {
                receiverTypeStr = "CLIENT";
            }

            NotificationType type = NotificationType.valueOf(typeStr.toUpperCase());
            NotificationItem.ReceiverType receiverType = NotificationItem.ReceiverType.valueOf(receiverTypeStr.toUpperCase());

            notificationService.sendBatchNotifications(userIds, receiverType, title, content, type);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "批量推送成功",
                "count", userIds.size()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "参数错误：" + e.getMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "推送失败：" + e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "标记通知为已读")
    public ResponseEntity<NotificationItem> markAsRead(@PathVariable String id) {
        NotificationItem notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/user/{userId}/mark-all-read")
    @Operation(summary = "标记用户所有通知为已读")
    public ResponseEntity<Map<String, Object>> markAllAsRead(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "CLIENT") String receiverType
    ) {
        try {
            NotificationItem.ReceiverType parsedReceiverType = NotificationItem.ReceiverType.valueOf(receiverType.toUpperCase());
            notificationService.markAllAsRead(userId, parsedReceiverType);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "receiverType is invalid"
            ));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}