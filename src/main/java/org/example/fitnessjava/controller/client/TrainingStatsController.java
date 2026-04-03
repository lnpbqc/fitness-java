package org.example.fitnessjava.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.BodyAssessmentRecord;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.vo.TrainingStatsResponse;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.TrainingStatsService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/client/user/training-stats")
@Slf4j
@Tag(name = "客户端训练数据接口", description = "客户端训练统计数据和体测记录的增删改查接口")
public class TrainingStatsController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ClientService clientService;

    @Resource
    private TrainingStatsService trainingStatsService;

    @GetMapping
    @Operation(summary = "获取训练统计数据", description = "返回当月训练次数、当前体重、体重和肌肉量趋势数据")
    public TrainingStatsResponse getTrainingStats(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentUserId(token);
        return trainingStatsService.getTrainingStats(userId);
    }

    @GetMapping("/records")
    @Operation(summary = "获取体测记录列表", description = "返回当前用户的所有体测记录，按日期倒序")
    public List<BodyAssessmentRecord> getRecords(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentUserId(token);
        return trainingStatsService.getRecordsByUserId(userId);
    }

    @GetMapping("/records/{id}")
    @Operation(summary = "获取体测记录详情", description = "根据记录 ID 获取当前用户的体测记录详情")
    public BodyAssessmentRecord getRecord(
            @Parameter(description = "记录ID", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentUserId(token);
        return trainingStatsService.getRecordById(userId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "记录不存在"));
    }

    @PostMapping("/records")
    @Operation(summary = "创建体测记录", description = "为当前用户创建一条新的体测记录")
    public BodyAssessmentRecord createRecord(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody BodyAssessmentRecord record
    ) {
        Integer userId = getCurrentUserId(token);
        return trainingStatsService.createRecord(userId, record);
    }

    @PutMapping("/records/{id}")
    @Operation(summary = "更新体测记录", description = "更新当前用户的指定体测记录")
    public BodyAssessmentRecord updateRecord(
            @Parameter(description = "记录ID", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody BodyAssessmentRecord record
    ) {
        Integer userId = getCurrentUserId(token);
        return trainingStatsService.updateRecord(userId, id, record)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "记录不存在"));
    }

    @DeleteMapping("/records/{id}")
    @Operation(summary = "删除体测记录", description = "删除当前用户的指定体测记录")
    public void deleteRecord(
            @Parameter(description = "记录ID", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentUserId(token);
        trainingStatsService.deleteRecord(userId, id);
    }

    private Integer getCurrentUserId(String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户登录信息无效");
        }
        Client client = clientService.existUser(openid);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "当前用户不存在");
        }
        return client.getId();
    }
}
