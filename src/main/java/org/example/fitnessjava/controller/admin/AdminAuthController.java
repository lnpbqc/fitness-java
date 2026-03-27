package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.AdminUser;
import org.example.fitnessjava.pojo.vo.LoginRequest;
import org.example.fitnessjava.pojo.vo.LoginResponse;
import org.example.fitnessjava.service.AdminUserService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@Tag(name = "管理后台认证", description = "管理员登录、登出等认证接口")
public class AdminAuthController {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AdminUser user = adminUserService.authenticate(request.getUsername(), request.getPassword());
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户名或密码错误");
            return ResponseEntity.badRequest().body(error);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        LoginResponse response = new LoginResponse(
                token,
                user.getUsername(),
                user.getNickname(),
                user.getAvatar(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "管理员登出")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "登出成功");
        return ResponseEntity.ok(response);
    }
}
