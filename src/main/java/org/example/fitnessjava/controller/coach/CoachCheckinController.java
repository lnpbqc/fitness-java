package org.example.fitnessjava.controller.coach;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.dto.CoachCheckinScanRequest;
import org.example.fitnessjava.pojo.vo.CoachCheckinResponse;
import org.example.fitnessjava.service.CoachCheckinService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coach/checkins")
@Slf4j
@Tag(name = "教练端核销管理", description = "教练扫码核销接口")
public class CoachCheckinController {

    @Resource
    private CoachCheckinService coachCheckinService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachService coachService;

    @PostMapping("/scan")
    @Operation(summary = "扫码核销", description = "教练扫码后校验二维码、核销预约、扣减课时")
    public ResponseEntity<CoachCheckinResponse> scanCheckin(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody CoachCheckinScanRequest request
    ) {
        Coach coach = getCurrentCoach(token);
        CoachCheckinResponse response = coachCheckinService.scanAndCheckin(coach.getId(), request.getQrCode());
        return ResponseEntity.ok(response);
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
