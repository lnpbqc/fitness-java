package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.vo.UpdateClientProfileRequest;
import org.example.fitnessjava.pojo.vo.UpdateUserRequest;
import org.example.fitnessjava.pojo.vo.UserVO;
import org.example.fitnessjava.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "用户管理", description = "管理后台用户相关接口")
public class AdminUserController {

    @Resource
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "获取用户列表")
    public ResponseEntity<List<UserVO>> getUsers() {
        List<UserVO> users = clientService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    public ResponseEntity<UserVO> getUser(@PathVariable Integer id) {
        return clientService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改用户信息")
    public ResponseEntity<UserVO> updateUser(@PathVariable Integer id, @RequestBody UpdateUserRequest request) {
        return clientService.updateUser(id, request.getNickname(), request.getAvatar(), request.getPhone(), request.getRole())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "修改用户角色")
    public ResponseEntity<UserVO> updateUserRole(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String role = request.get("role");
        return clientService.updateUser(id, null, null, null, role)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client-profiles")
    @Operation(summary = "获取会员档案列表")
    public ResponseEntity<List<UserVO>> getClientProfiles() {
        List<UserVO> clients = clientService.getAllClientProfiles();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/client-profiles/{id}")
    @Operation(summary = "获取会员详情")
    public ResponseEntity<UserVO> getClientProfile(@PathVariable Integer id) {
        return clientService.getClientProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/client-profiles/{id}")
    @Operation(summary = "修改会员信息")
    public ResponseEntity<UserVO> updateClientProfile(@PathVariable Integer id, @RequestBody UpdateClientProfileRequest request) {
        return clientService.updateClientProfile(id, request.getMemberLevel(), request.getPoints(), request.getMembershipExpireAt())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/client-profiles/{id}/points")
    @Operation(summary = "调整会员积分")
    public ResponseEntity<UserVO> updateClientPoints(@PathVariable Integer id, @RequestBody Map<String, Integer> request) {
        Integer points = request.get("points");
        return clientService.updateClientProfile(id, null, points, null)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/client-profiles/{id}/level")
    @Operation(summary = "调整会员等级")
    public ResponseEntity<UserVO> updateClientLevel(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String level = request.get("level");
        return clientService.updateClientProfile(id, level, null, null)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/convert-to-coach")
    @Operation(summary = "将用户转换为教练")
    public ResponseEntity<Map<String, Object>> convertToCoach(@PathVariable Integer id) {
        try {
            clientService.convertUserToCoach(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户已成功转换为教练");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "转换失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
