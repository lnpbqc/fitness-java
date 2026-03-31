package org.example.fitnessjava.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.ProductOrder;
import org.example.fitnessjava.pojo.vo.ProductOrderVO;
import org.example.fitnessjava.service.ProductOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/product-orders")
@Tag(name = "商品订单管理", description = "管理后台商品订单相关接口")
public class AdminProductOrderController {

    @Resource
    private ProductOrderService productOrderService;

    @GetMapping
    @Operation(summary = "获取商品订单列表")
    public ResponseEntity<List<ProductOrderVO>> getOrders() {
        List<ProductOrderVO> orders = productOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品订单详情")
    public ResponseEntity<ProductOrderVO> getOrder(@PathVariable Long id) {
        return productOrderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建商品订单")
    public ResponseEntity<ProductOrder> createOrder(@RequestBody ProductOrder request) {
        ProductOrder order = productOrderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品订单")
    public ResponseEntity<ProductOrder> updateOrder(@PathVariable Long id, @RequestBody ProductOrder request) {
        return productOrderService.updateOrder(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "商品订单退款")
    public ResponseEntity<ProductOrder> refundOrder(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        return productOrderService.refundOrder(id, reason)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/ship")
    @Operation(summary = "商品订单发货")
    public ResponseEntity<ProductOrder> shipOrder(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String trackingNumber = request.get("trackingNumber");
        return productOrderService.shipOrder(id, trackingNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品订单")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        productOrderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
