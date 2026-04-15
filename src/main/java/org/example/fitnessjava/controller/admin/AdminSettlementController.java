package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.vo.AdminSettlementListItemVO;
import org.example.fitnessjava.pojo.vo.AdminSettlementListResponseVO;
import org.example.fitnessjava.pojo.vo.AdminSettlementSummaryVO;
import org.example.fitnessjava.service.AdminSettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/settlements")
@Tag(name = "教练结算流水", description = "管理后台教练结算流水接口")
public class AdminSettlementController {

    @Resource
    private AdminSettlementService adminSettlementService;

    @GetMapping
    @Operation(summary = "获取教练结算流水列表")
    public ResponseEntity<AdminSettlementListResponseVO> getSettlements(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer coachId,
            @RequestParam(required = false) Integer bookingId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(adminSettlementService.getSettlements(
                startDate,
                endDate,
                coachId,
                bookingId,
                page,
                pageSize
        ));
    }

    @GetMapping("/summary")
    @Operation(summary = "获取教练结算流水汇总")
    public ResponseEntity<AdminSettlementSummaryVO> getSummary(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer coachId,
            @RequestParam(required = false) Integer bookingId
    ) {
        return ResponseEntity.ok(adminSettlementService.getSummary(
                startDate,
                endDate,
                coachId,
                bookingId
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取教练结算流水详情")
    public ResponseEntity<AdminSettlementListItemVO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(adminSettlementService.getDetail(id));
    }
}
