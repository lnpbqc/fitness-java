package org.example.fitnessjava.controller.coach;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.vo.CoachPerformanceSummaryVO;
import org.example.fitnessjava.service.CoachPerformanceService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coach/performance")
@Slf4j
@Tag(name = "教练端绩效管理", description = "教练业绩统计接口")
public class CoachPerformanceController {

    @Resource
    private CoachPerformanceService coachPerformanceService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachService coachService;

    @GetMapping("/summary")
    @Operation(summary = "获取业绩统计数据", description = "获取教练业绩统计数据")
    public ResponseEntity<CoachPerformanceSummaryVO> getPerformanceSummary(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        Coach coach = getCurrentCoach(token);
        CoachPerformanceSummaryVO summary = coachPerformanceService.getPerformanceSummary(coach.getId());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/trend")
    @Operation(summary = "获取收入趋势数据", description = "获取教练收入趋势数据")
    public ResponseEntity<List<Map<String, Object>>> getPerformanceTrend(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        Coach coach = getCurrentCoach(token);
        List<Map<String, Object>> trend = coachPerformanceService.getPerformanceTrend(coach.getId());
        return ResponseEntity.ok(trend);
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
