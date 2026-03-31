package org.example.fitnessjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.vo.CourseOrderVO;
import org.example.fitnessjava.service.CourseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api//course-order")
@Tag(name = "课程订单管理", description = "管理课程订单相关接口")
public class CourseOrderController {

    @Resource
    private CourseOrderService courseOrderService;

    @GetMapping
    @Operation(summary = "获取课程订单列表")
    public ResponseEntity<List<CourseOrderVO>> getOrders() {
        List<CourseOrderVO> orders = courseOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取课程订单详情")
    public ResponseEntity<CourseOrderVO> getOrder(@PathVariable Long id) {
        return courseOrderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建课程订单")
    public ResponseEntity<CourseOrder> createOrder(@RequestBody CourseOrder request) {
        CourseOrder order = courseOrderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新课程订单")
    public ResponseEntity<CourseOrder> updateOrder(@PathVariable Long id, @RequestBody CourseOrder request) {
        return courseOrderService.updateOrder(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "课程订单退款")
    public ResponseEntity<CourseOrder> refundOrder(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        return courseOrderService.refundOrder(id, reason)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/sessions")
    @Operation(summary = "调整课时")
    public ResponseEntity<CourseOrder> updateSessions(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer sessions = request.get("sessions");
        return courseOrderService.updateSessions(id, sessions)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程订单")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        courseOrderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
