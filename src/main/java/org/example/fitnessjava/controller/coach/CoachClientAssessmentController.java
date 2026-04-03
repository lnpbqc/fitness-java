package org.example.fitnessjava.controller.coach;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.BodyAssessmentRecord;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.dao.BodyAssessmentRecordRepository;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach/clients")
@Slf4j
@Tag(name = "教练端学员体测管理", description = "教练查看和录入学员体测数据接口")
public class CoachClientAssessmentController {

    @Resource
    private BodyAssessmentRecordRepository bodyAssessmentRecordRepository;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CoachService coachService;

    @Resource
    private ClientService clientService;

    @GetMapping("/{clientId}/assessment-records")
    @Operation(summary = "获取学员体测记录列表", description = "获取指定学员的体测记录列表")
    public ResponseEntity<List<BodyAssessmentRecord>> getAssessmentRecords(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "学员 ID", required = true)
            @PathVariable Integer clientId
    ) {
        Coach coach = getCurrentCoach(token);
        List<BodyAssessmentRecord> records = bodyAssessmentRecordRepository.findByUserIdAndCoachId(clientId, coach.getId());
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{clientId}/assessment-records/{recordId}")
    @Operation(summary = "获取体测记录详情", description = "获取指定体测记录详情")
    public ResponseEntity<BodyAssessmentRecord> getAssessmentRecord(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "学员 ID", required = true)
            @PathVariable Integer clientId,
            @Parameter(description = "体测记录 ID", required = true)
            @PathVariable Integer recordId
    ) {
        Coach coach = getCurrentCoach(token);
        BodyAssessmentRecord record = bodyAssessmentRecordRepository.findByIdAndUserId(recordId, clientId)
                .orElseThrow(() -> new IllegalArgumentException("体测记录不存在"));
        return ResponseEntity.ok(record);
    }

    @PostMapping("/{clientId}/assessment-records")
    @Operation(summary = "录入体测数据", description = "教练为指定学员录入体测数据")
    public ResponseEntity<BodyAssessmentRecord> createAssessmentRecord(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "学员 ID", required = true)
            @PathVariable Integer clientId,
            @RequestBody BodyAssessmentRecord request
    ) {
        Coach coach = getCurrentCoach(token);
        request.setUserId(clientId);
        request.setCoachId(coach.getId());
        BodyAssessmentRecord saved = bodyAssessmentRecordRepository.save(request);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{clientId}/assessment-records/{recordId}")
    @Operation(summary = "更新体测数据", description = "教练更新指定学员的体测数据")
    public ResponseEntity<BodyAssessmentRecord> updateAssessmentRecord(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "学员 ID", required = true)
            @PathVariable Integer clientId,
            @Parameter(description = "体测记录 ID", required = true)
            @PathVariable Integer recordId,
            @RequestBody BodyAssessmentRecord request
    ) {
        Coach coach = getCurrentCoach(token);
        BodyAssessmentRecord existing = bodyAssessmentRecordRepository.findByIdAndUserId(recordId, clientId)
                .orElseThrow(() -> new IllegalArgumentException("体测记录不存在"));

        if (request.getHeight() != null) existing.setHeight(request.getHeight());
        if (request.getWeight() != null) existing.setWeight(request.getWeight());
        if (request.getBodyFat() != null) existing.setBodyFat(request.getBodyFat());
        if (request.getMuscleMass() != null) existing.setMuscleMass(request.getMuscleMass());
        if (request.getBmi() != null) existing.setBmi(request.getBmi());
        if (request.getVisceralFat() != null) existing.setVisceralFat(request.getVisceralFat());
        if (request.getChest() != null) existing.setChest(request.getChest());
        if (request.getWaist() != null) existing.setWaist(request.getWaist());
        if (request.getHips() != null) existing.setHips(request.getHips());
        if (request.getLeftArm() != null) existing.setLeftArm(request.getLeftArm());
        if (request.getRightArm() != null) existing.setRightArm(request.getRightArm());
        if (request.getLeftThigh() != null) existing.setLeftThigh(request.getLeftThigh());
        if (request.getRightThigh() != null) existing.setRightThigh(request.getRightThigh());
        if (request.getLeftCalf() != null) existing.setLeftCalf(request.getLeftCalf());
        if (request.getRightCalf() != null) existing.setRightCalf(request.getRightCalf());
        if (request.getPhotos() != null) existing.setPhotos(request.getPhotos());
        if (request.getNotes() != null) existing.setNotes(request.getNotes());

        BodyAssessmentRecord saved = bodyAssessmentRecordRepository.save(existing);
        return ResponseEntity.ok(saved);
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
