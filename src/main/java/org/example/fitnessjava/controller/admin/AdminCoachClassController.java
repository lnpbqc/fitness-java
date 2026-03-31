package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.CoachClass;
import org.example.fitnessjava.pojo.CoachClassStatus;
import org.example.fitnessjava.pojo.vo.CoachClassVO;
import org.example.fitnessjava.service.CoachClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/coach-classes")
@Tag(name = "教练课程记录管理", description = "管理后台教练课程记录相关接口")
public class AdminCoachClassController {

    @Resource
    private CoachClassService coachClassService;

    @GetMapping
    @Operation(summary = "获取教练课程记录列表")
    public ResponseEntity<List<CoachClassVO>> getCoachClasses(
            @RequestParam(required = false) Integer coachId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer clientId) {
        
        List<CoachClassVO> records;
        
        if (coachId != null || status != null || clientId != null) {
            CoachClassStatus classStatus = status != null ? CoachClassStatus.valueOf(status) : null;
            records = coachClassService.getCoachClassesWithInfo(coachId, classStatus, clientId);
        } else {
            records = coachClassService.getAllCoachClassesWithInfo();
        }
        
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取课程详情")
    public ResponseEntity<CoachClassVO> getCoachClass(@PathVariable Integer id) {
        CoachClassVO record = coachClassService.getCoachClassByIdWithInfo(id);
        return record != null ? ResponseEntity.ok(record) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "创建课程记录")
    public ResponseEntity<CoachClass> createCoachClass(@RequestBody CoachClass coachClass) {
        CoachClass record = coachClassService.createCoachClass(coachClass);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新课程记录")
    public ResponseEntity<CoachClass> updateCoachClass(
            @PathVariable Integer id,
            @RequestBody CoachClass coachClass) {
        try {
            CoachClass record = coachClassService.updateCoachClass(coachClass);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新课程状态")
    public ResponseEntity<CoachClass> updateCoachClassStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        CoachClassStatus classStatus = CoachClassStatus.valueOf(status);
        try {
            CoachClass record = coachClassService.updateCoachClassStatus(id, classStatus);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程记录")
    public ResponseEntity<Void> deleteCoachClass(@PathVariable Integer id) {
        coachClassService.deleteCoachClass(id);
        return ResponseEntity.ok().build();
    }
}
