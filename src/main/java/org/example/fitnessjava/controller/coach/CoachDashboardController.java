package org.example.fitnessjava.controller.coach;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.vo.CoachDashboardVO;
import org.example.fitnessjava.service.CoachDashboardService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coach/dashboard")
@Slf4j
@Tag(name = "教练端首页概览", description = "教练工作台首页数据接口")
public class CoachDashboardController {

    @Resource
    private CoachDashboardService coachDashboardService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachService coachService;

    @GetMapping
    @Operation(summary = "获取教练工作台首页数据", description = "获取教练工作台首页数据")
    public ResponseEntity<CoachDashboardVO> getDashboard(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        Coach coach = getCurrentCoach(token);
        CoachDashboardVO dashboard = coachDashboardService.getDashboard(coach.getId());
        return ResponseEntity.ok(dashboard);
    }

    private Coach getCurrentCoach(String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new IllegalArgumentException("无效的登录 token");
        }
        return coachService.getCoachByOpenid(openid)
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));
    }
}
