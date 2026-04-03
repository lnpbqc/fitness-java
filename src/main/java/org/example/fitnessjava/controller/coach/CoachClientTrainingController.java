package org.example.fitnessjava.controller.coach;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.TrainingRecord;
import org.example.fitnessjava.dao.TrainingRecordRepository;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach/clients")
@Slf4j
@Tag(name = "教练端学员训练管理", description = "教练查看学员训练记录接口")
public class CoachClientTrainingController {

    @Resource
    private TrainingRecordRepository trainingRecordRepository;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachService coachService;

    @Resource
    private ClientService clientService;

    @GetMapping("/{clientId}/training-records")
    @Operation(summary = "获取学员训练记录", description = "获取指定学员的训练记录")
    public ResponseEntity<List<TrainingRecord>> getTrainingRecords(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "学员 ID", required = true)
            @PathVariable Integer clientId
    ) {
        Coach coach = getCurrentCoach(token);
        List<TrainingRecord> records = trainingRecordRepository.findByClientIdAndCoachId(clientId, coach.getId());
        return ResponseEntity.ok(records);
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
