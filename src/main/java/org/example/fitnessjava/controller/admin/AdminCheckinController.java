package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.CheckinTicket;
import org.example.fitnessjava.pojo.TicketStatus;
import org.example.fitnessjava.pojo.vo.CheckinTicketVO;
import org.example.fitnessjava.service.CheckinTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/checkin-records")
@Tag(name = "核销记录管理", description = "管理后台核销记录相关接口")
public class AdminCheckinController {

    @Resource
    private CheckinTicketService checkinTicketService;

    @GetMapping
    @Operation(summary = "获取核销记录列表")
    public ResponseEntity<List<CheckinTicketVO>> getCheckinRecords(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer memberId) {
        
        List<CheckinTicketVO> records;
        
        if (status != null || memberId != null) {
            TicketStatus ticketStatus = status != null ? TicketStatus.valueOf(status) : null;
            records = checkinTicketService.getCheckinTicketsWithInfo(ticketStatus, memberId);
        } else {
            records = checkinTicketService.getAllCheckinTicketsWithInfo();
        }
        
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取核销记录详情")
    public ResponseEntity<CheckinTicketVO> getCheckinRecord(@PathVariable Integer id) {
        CheckinTicketVO record = checkinTicketService.getCheckinTicketByIdWithInfo(id);
        return record != null ? ResponseEntity.ok(record) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "创建核销记录")
    public ResponseEntity<CheckinTicket> createCheckinRecord(@RequestBody CheckinTicket checkinTicket) {
        CheckinTicket record = checkinTicketService.createCheckinTicket(checkinTicket);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新核销状态")
    public ResponseEntity<CheckinTicket> updateCheckinStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        TicketStatus ticketStatus = TicketStatus.valueOf(status);
        try {
            CheckinTicket record = checkinTicketService.updateCheckinTicketStatus(id, ticketStatus);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除核销记录")
    public ResponseEntity<Void> deleteCheckinRecord(@PathVariable Integer id) {
        checkinTicketService.deleteCheckinTicket(id);
        return ResponseEntity.ok().build();
    }
}
