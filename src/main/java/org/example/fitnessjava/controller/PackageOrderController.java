package org.example.fitnessjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.PackageOrder;
import org.example.fitnessjava.pojo.vo.PackageOrderVO;
import org.example.fitnessjava.service.PackageOrderService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/package-order")
@Tag(name = "套餐订单管理", description = "管理套餐订单相关接口")
@Slf4j
public class PackageOrderController {

    @Resource
    private PackageOrderService packageOrderService;
    @Resource
    private JwtUtil jwtUtil;

    @GetMapping
    @Operation(summary = "获取套餐订单列表")
    public ResponseEntity<List<PackageOrderVO>> getOrders() {
        List<PackageOrderVO> orders = packageOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户的套餐订单列表")
    public List<PackageOrderVO> getOrdersOfMine(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("请检查请求头");
        }
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        List<PackageOrderVO> orders = packageOrderService.getOrdersOfMine(openid);
        log.info("orders: {}", orders);
        return orders;
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取套餐订单详情")
    public ResponseEntity<PackageOrderVO> getOrder(@PathVariable Long id) {
        return packageOrderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建套餐订单")
    public ResponseEntity<PackageOrder> createOrder(@RequestBody PackageOrder request) {
        PackageOrder order = packageOrderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新套餐订单")
    public ResponseEntity<PackageOrder> updateOrder(@PathVariable Long id, @RequestBody PackageOrder request) {
        return packageOrderService.updateOrder(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "套餐订单退款")
    public ResponseEntity<PackageOrder> refundOrder(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        return packageOrderService.refundOrder(id, reason)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/sessions")
    @Operation(summary = "调整课时")
    public ResponseEntity<PackageOrder> updateSessions(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer sessions = request.get("sessions");
        return packageOrderService.updateSessions(id, sessions)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除套餐订单")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        packageOrderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
