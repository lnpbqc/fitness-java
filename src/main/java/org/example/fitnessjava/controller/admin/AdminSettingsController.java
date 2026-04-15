package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.vo.CoachCompensationSettingsVO;
import org.example.fitnessjava.pojo.vo.UpdateCoachCompensationSettingsRequest;
import org.example.fitnessjava.service.CoachCompensationService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/settings")
@Tag(name = "系统设置", description = "管理后台系统设置接口")
public class AdminSettingsController {

    @Resource
    private CoachCompensationService coachCompensationService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/coach-compensation")
    @Operation(summary = "获取教练薪酬配置")
    public ResponseEntity<CoachCompensationSettingsVO> getCoachCompensationSettings() {
        return ResponseEntity.ok(coachCompensationService.getSettings());
    }

    @PutMapping("/coach-compensation")
    @Operation(summary = "更新教练薪酬配置")
    public ResponseEntity<CoachCompensationSettingsVO> updateCoachCompensationSettings(
            @RequestBody UpdateCoachCompensationSettingsRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        String updatedBy = jwtUtil.getSubjectFromAuthorization(authorization);
        if (updatedBy == null || updatedBy.isBlank()) {
            updatedBy = "admin";
        }

        CoachCompensationSettingsVO updated = coachCompensationService.updateSettings(
                request.getLessonUnitPrice(),
                request.getCoachRatio(),
                updatedBy
        );
        return ResponseEntity.ok(updated);
    }
}
