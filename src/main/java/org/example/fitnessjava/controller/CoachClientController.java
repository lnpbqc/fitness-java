package org.example.fitnessjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/coach/clients")
@Slf4j
@Tag(name = "教练端客户管理", description = "教练查看已绑定客户列表")
public class CoachClientController {

    @Resource
    private CoachService coachService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "获取当前教练的客户列表", description = "根据 Authorization token 获取当前教练已绑定的所有客户")
    public ArrayList<Client> getMyClients(
            @Parameter(description = "教练登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            return new ArrayList<>();
        }
        Coach coach = coachService.getCoachByOpenid(openid).orElse(null);
        if (coach == null) {
            return new ArrayList<>();
        }
        return coachService.getClientsByCoachId(coach.getId());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取客户详情", description = "获取指定客户的详细信息")
    public Map<String, Object> getClientDetail(@PathVariable Integer id) {
        return clientService.getUserById(id)
                .map(user -> Map.of("success", true, "user", user))
                .orElse(Map.of("success", false, "message", "客户不存在"));
    }
}
