package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.service.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/coaches")
@Tag(name = "教练管理", description = "管理后台教练相关接口")
public class AdminCoachController {

    @Resource
    private CoachService coachService;

    @GetMapping
    @Operation(summary = "获取教练列表")
    public ResponseEntity<List<Coach>> getCoaches() {
        List<Coach> coaches = coachService.getAllCoaches();
        return ResponseEntity.ok(coaches);
    }

    @GetMapping("/unverified")
    @Operation(summary = "获取未校验教练列表")
    public ResponseEntity<List<Coach>> getUnverifiedCoaches() {
        List<Coach> unverified = coachService.getUnverifiedCoaches();
        return ResponseEntity.ok(unverified);
    }

    @PostMapping
    @Operation(summary = "创建教练")
    public ResponseEntity<Coach> createCoach(@RequestBody Coach request) {
        Coach coach = coachService.createCoach(request);
        return ResponseEntity.ok(coach);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取教练详情")
    public ResponseEntity<Coach> getCoach(@PathVariable Long id) {
        return coachService.getCoachById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改教练信息")
    public ResponseEntity<Coach> updateCoach(@PathVariable Long id, @RequestBody Coach request) {
        return coachService.updateCoach(id, request.getNickname(), request.getAvatar(), request.getIntro(),
                request.getSpecialty(), request.getDescription(), request.getRating(), 
                request.getLevel(), request.getPhone(), request.getLocation(), request.getPosterUrl(), request.getStatus(), request.getFeatured())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/featured")
    @Operation(summary = "设置推荐教练")
    public ResponseEntity<Coach> updateCoachFeatured(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        Boolean featured = request.get("featured");
        return coachService.updateCoachFeatured(id, featured)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "修改教练状态")
    public ResponseEntity<Coach> updateCoachStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Coach.Status coachStatus = Coach.Status.valueOf(status);
        return coachService.updateCoachStatus(id, coachStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除教练")
    public ResponseEntity<Void> deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/verify")
    @Operation(summary = "设置教练校验状态")
    public ResponseEntity<Coach> updateCoachVerify(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        Boolean verified = request.get("verified");
        return coachService.updateCoachVerified(id, verified)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
