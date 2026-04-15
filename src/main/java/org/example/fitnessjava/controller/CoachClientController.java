package org.example.fitnessjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.BodyAssessmentRecordRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.CoachWithUserRepository;
import org.example.fitnessjava.dao.PackageOrderRepository;
import org.example.fitnessjava.dao.TrainingRecordRepository;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.vo.CoachClientDetailVO;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/coach/clients")
@Slf4j
@Tag(name = "教练端客户管理", description = "教练查看已绑定客户列表及详情")
public class CoachClientController {

    @Resource
    private CoachService coachService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ClientService clientService;

    @Resource
    private CoachWithUserRepository coachWithUserRepository;

    @Resource
    private CoachRepository coachRepository;

    @Resource
    private PackageOrderRepository packageOrderRepository;

    @Resource
    private TrainingRecordRepository trainingRecordRepository;

    @Resource
    private BodyAssessmentRecordRepository bodyAssessmentRecordRepository;

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

    @PostMapping("/bind")
    @Operation(summary = "教练添加学员", description = "教练通过手机号或学员ID绑定学员")
    public ResponseEntity<Map<String, Object>> bindClient(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody String clientQRCode
    ) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "无效的登录 token"));
        }
        Coach coach = coachService.getCoachByOpenid(openid)
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));
        Client client = null;
        CoachWithUser relation =  coachService.addCoachWithUser(coach.getId(),clientQRCode).orElse(null);
        if(relation == null) {
            throw new RuntimeException("建立关联关系失败");
        }
        client = clientService.existUserByUserId(relation.getClientId());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "绑定成功",
                "client", client
        ));
    }

    @DeleteMapping("/{clientId}/unbind")
    @Operation(summary = "教练解绑学员", description = "教练解除与学员的绑定关系")
    public ResponseEntity<Map<String, Object>> unbindClient(
            @Parameter(description = "教练登录 token", required = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Integer clientId
    ) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "无效的登录 token"));
        }
        Coach coach = coachService.getCoachByOpenid(openid)
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));

        ArrayList<CoachWithUser> relations = coachWithUserRepository.findAllByCoachIdAndClientId(coach.getId(), clientId);
        if (relations.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "未绑定该学员"));
        }

        coachWithUserRepository.deleteAll(relations);
        return ResponseEntity.ok(Map.of("success", true, "message", "解绑成功"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取客户详情", description = "获取指定客户的详细信息，包含会员档案、课程订单、教练绑定、训练及体测摘要")
    public CoachClientDetailVO getClientDetail(
            @Parameter(description = "教练登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id
    ) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new IllegalArgumentException("无效的登录 token");
        }
        Coach coach = coachService.getCoachByOpenid(openid)
                .orElseThrow(() -> new IllegalArgumentException("教练不存在"));

        Client client = clientService.existUserByUserId(id);
        if (client == null) {
            throw new IllegalArgumentException("客户不存在");
        }

        return buildClientDetailVO(client, coach.getId());
    }

    private CoachClientDetailVO buildClientDetailVO(Client client, Integer coachId) {
        CoachClientDetailVO vo = new CoachClientDetailVO();
        vo.setId(client.getId());
        vo.setNickname(client.getNickname());
        vo.setAvatar(client.getAvatar());
        vo.setPhone(client.getPhone());
        vo.setGender(client.getGender());
        vo.setAge(client.getAge());
        vo.setTags(client.getTags());
        vo.setMemberLevel(client.getMemberLevel());
        vo.setPoints(client.getPoints());
        vo.setMembershipExpireAt(client.getMembershipExpireAt());
        vo.setJoinDate(client.getJoinDate());

        vo.setActiveCourse(buildActiveCourse(client.getId()));
        vo.setCoachBinding(buildCoachBinding(coachId));
        vo.setTrainingSummary(buildTrainingSummary(client.getId()));
        vo.setAssessmentSummary(buildAssessmentSummary(client.getId()));

        return vo;
    }

    private CoachClientDetailVO.ActiveCourseSummary buildActiveCourse(Integer clientId) {
        List<PackageOrder> orders = packageOrderRepository.findByUserId(clientId);
        Optional<PackageOrder> activeOrder = orders.stream()
                .filter(o -> o.getStatus() == PackageOrderStatus.ACTIVE)
                .sorted(Comparator.comparing(PackageOrder::getId).reversed())
                .findFirst();

        if (activeOrder.isEmpty()) {
            return null;
        }

        PackageOrder order = activeOrder.get();
        CoachClientDetailVO.ActiveCourseSummary summary = new CoachClientDetailVO.ActiveCourseSummary();
        summary.setPackageName(order.getPackageName());
        summary.setRemainingSessions(order.getRemainingSessions());
        summary.setTotalSessions(order.getTotalSessions());
        summary.setEndDate(order.getEndDate());
        return summary;
    }

    private CoachClientDetailVO.CoachBindingInfo buildCoachBinding(Integer coachId) {
        Optional<Coach> coachOpt = coachRepository.findById(Long.valueOf(coachId));
        if (coachOpt.isEmpty()) {
            return null;
        }

        Coach coach = coachOpt.get();
        CoachClientDetailVO.CoachBindingInfo info = new CoachClientDetailVO.CoachBindingInfo();
        info.setCoachId(coach.getId());
        info.setCoachName(coach.getNickname());
        info.setCoachAvatar(coach.getAvatar());
        info.setSpecialty(coach.getSpecialty());
        return info;
    }

    private CoachClientDetailVO.TrainingSummary buildTrainingSummary(Integer clientId) {
        List<TrainingRecord> records = trainingRecordRepository.findByClientId(clientId);
        if (records.isEmpty()) {
            return null;
        }

        Optional<TrainingRecord> latest = records.stream()
                .filter(r -> r.getDate() != null)
                .sorted(Comparator.comparing(TrainingRecord::getDate).reversed())
                .findFirst();

        CoachClientDetailVO.TrainingSummary summary = new CoachClientDetailVO.TrainingSummary();
        summary.setTotalTrainingCount(records.size());
        latest.ifPresent(r -> summary.setLastTrainingDate(r.getDate()));
        return summary;
    }

    private CoachClientDetailVO.AssessmentSummary buildAssessmentSummary(Integer clientId) {
        List<BodyAssessmentRecord> records = bodyAssessmentRecordRepository.findByUserId(clientId);
        if (records.isEmpty()) {
            return null;
        }

        Optional<BodyAssessmentRecord> latest = records.stream()
                .filter(r -> r.getDate() != null)
                .sorted(Comparator.comparing(BodyAssessmentRecord::getDate).reversed())
                .findFirst();

        CoachClientDetailVO.AssessmentSummary summary = new CoachClientDetailVO.AssessmentSummary();
        summary.setTotalAssessmentCount(records.size());
        latest.ifPresent(r -> {
            summary.setLastAssessmentDate(r.getDate());
            summary.setLatestWeight(r.getWeight());
            summary.setLatestBodyFat(r.getBodyFat());
        });
        return summary;
    }
}
