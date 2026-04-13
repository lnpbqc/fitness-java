package org.example.fitnessjava.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.pojo.dto.ClientOrderRequest;
import org.example.fitnessjava.pojo.vo.PackageOrderVO;
import org.example.fitnessjava.pojo.vo.ProductOrderVO;
import org.example.fitnessjava.service.*;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/client/orders")
@Slf4j
@Tag(name = "客户端订单接口", description = "客户端套餐订单与商品订单的创建、查询、退款接口")
public class OrderController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ClientService clientService;

    @Resource
    private PackageOrderService packageOrderService;

    @Resource
    private ProductOrderService productOrderService;

    @Resource
    private PackageService packageService;

    @Resource
    private ProductService productService;

    @PostMapping
    @Operation(summary = "创建订单", description = "创建套餐订单(type=PACKAGE)或商品订单(type=SHOP)")
    public Map<String, Object> createOrder(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody ClientOrderRequest request
    ) {
        log.info("创建订单请求: type={}, packageId={}, items={}, pointsUsed={}",
                request.getType(), request.getPackageId(), request.getItems(), request.getPointsUsed());

        Integer userId = getCurrentClientId(token);
        String type = request.getType();

        if (type == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "订单类型不能为空");
        }

        Map<String, Object> result = new HashMap<>();

        if ("PACKAGE".equalsIgnoreCase(type)) {
            PackageOrder order = createPackageOrder(userId, request);
            result.put("type", "PACKAGE");
            result.put("order", packageOrderService.getOrderById((long) order.getId()).orElse(null));
        } else if ("SHOP".equalsIgnoreCase(type)) {
            ProductOrder order = createProductOrder(userId, request);
            result.put("type", "SHOP");
            result.put("order", productOrderService.getOrderById((long) order.getId()).orElse(null));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "不支持的订单类型: " + type);
        }

        return result;
    }

    @GetMapping
    @Operation(summary = "获取当前用户订单列表", description = "返回当前用户的套餐订单和商品订单，按日期倒序排列")
    public Map<String, Object> listOrders(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentClientId(token);

        List<PackageOrderVO> packageOrders = packageOrderService.getOrdersByUserId(userId);
        List<ProductOrderVO> productOrders = productOrderService.getOrdersByUserId(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("packageOrders", packageOrders);
        result.put("productOrders", productOrders);
        return result;
    }

    @GetMapping("/course/{id}")
    @Operation(summary = "获取套餐订单详情", description = "根据订单ID获取当前用户的套餐订单详情")
    public PackageOrderVO getPackageOrder(
            @Parameter(description = "订单ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentClientId(token);
        PackageOrderVO vo = packageOrderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在"));
        if (!vo.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权查看该订单");
        }
        return vo;
    }

    @GetMapping("/product/{id}")
    @Operation(summary = "获取商品订单详情", description = "根据订单ID获取当前用户的商品订单详情")
    public ProductOrderVO getProductOrder(
            @Parameter(description = "订单ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = getCurrentClientId(token);
        ProductOrderVO vo = productOrderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在"));
        if (!vo.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权查看该订单");
        }
        return vo;
    }

    @PostMapping("/course/{id}/refund")
    @Operation(summary = "套餐订单退款", description = "申请套餐订单退款，将状态设为退款中")
    public PackageOrderVO refundPackageOrder(
            @Parameter(description = "订单ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody(required = false) Map<String, String> body
    ) {
        Integer userId = getCurrentClientId(token);
        PackageOrderVO vo = packageOrderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在"));
        if (!vo.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权操作该订单");
        }
        if (vo.getStatus() != PackageOrderStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "当前状态不允许退款");
        }
        String reason = body != null ? body.getOrDefault("reason", "用户申请退款") : "用户申请退款";
        packageOrderService.refundOrder(id, reason);
        return packageOrderService.getOrderById(id).orElse(null);
    }

    @PostMapping("/product/{id}/refund")
    @Operation(summary = "商品订单退款", description = "申请商品订单退款，仅在已付款状态下可申请")
    public ProductOrderVO refundProductOrder(
            @Parameter(description = "订单ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody(required = false) Map<String, String> body
    ) {
        Integer userId = getCurrentClientId(token);
        ProductOrderVO vo = productOrderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在"));
        if (!vo.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权操作该订单");
        }
        if (vo.getStatus() != ProductOrderStatus.PENDING && vo.getStatus() != ProductOrderStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "当前状态不允许退款");
        }
        String reason = body != null ? body.getOrDefault("reason", "用户申请退款") : "用户申请退款";
        productOrderService.refundOrder(id, reason);
        return productOrderService.getOrderById(id).orElse(null);
    }

    private PackageOrder createPackageOrder(Integer userId, ClientOrderRequest request) {
        Integer packageId = request.getPackageId();
        if (packageId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "套餐ID不能为空");
        }

        Package pkg = packageService.getPackageById((long) packageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "套餐不存在"));
        if (pkg.getSaleStatus() != SaleStatus.ON_SALE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "套餐已下架");
        }

        Client client = clientService.existUserByUserId(userId);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户不存在");
        }

        Integer pointsUsed = request.getPointsUsed() != null ? request.getPointsUsed() : 0;
        if (pointsUsed > 0) {
            if (client.getPoints() < pointsUsed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "积分不足");
            }
            client.setPoints(client.getPoints() - pointsUsed);
        }

        double actualPay = Math.max(pkg.getPrice() - pointsUsed / 10.0, 0);

        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(pkg.getValidDays());

        PackageOrder order = new PackageOrder();
        order.setUserId(userId);
        order.setPackageId(pkg.getId());
        order.setPackageName(pkg.getName());
        order.setType(pkg.getType());
        order.setTotalSessions(pkg.getSessions());
        order.setUsedSessions(0);
        order.setRemainingSessions(pkg.getSessions());
        order.setValidDays(pkg.getValidDays());
        order.setStartDate(now.format(DateTimeFormatter.ISO_LOCAL_DATE));
        order.setEndDate(endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        order.setPurchaseDate(now.format(DateTimeFormatter.ISO_LOCAL_DATE));
        order.setPrice(pkg.getPrice());
        order.setPointsUsed(pointsUsed);
        order.setActualPay(actualPay);
        order.setPointsReward(pkg.getPointsReward());
        order.setStatus(PackageOrderStatus.ACTIVE);

        PackageOrder savedOrder = packageOrderService.createOrder(order);

        if (pkg.getPointsReward() != null && pkg.getPointsReward() > 0) {
            client.setPoints(client.getPoints() + pkg.getPointsReward());
        }
        clientService.updateClient(client);

        return savedOrder;
    }

    private ProductOrder createProductOrder(Integer userId, ClientOrderRequest request) {
        List<ClientOrderRequest.OrderItemRequest> items = request.getItems();
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "商品订单至少需要一个商品");
        }

        List<ProductOrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;
        int totalPointsReward = 0;

        for (ClientOrderRequest.OrderItemRequest itemReq : items) {
            if (itemReq.getProductId() == null || itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "商品信息不完整");
            }

            Product product = productService.getProductById((long) itemReq.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "商品不存在: ID=" + itemReq.getProductId()));
            if (product.getSaleStatus() != SaleStatus.ON_SALE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "商品已下架: " + product.getName());
            }
            if (product.getStock() != null && product.getStock() < itemReq.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "库存不足: " + product.getName() + "，剩余 " + product.getStock() + " 件");
            }

            ProductOrderItem orderItem = new ProductOrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setName(product.getName());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setImage(product.getImage());
            orderItems.add(orderItem);

            totalAmount += product.getPrice() * itemReq.getQuantity();
            if (product.getPointsReward() != null) {
                totalPointsReward += product.getPointsReward() * itemReq.getQuantity();
            }
        }

        Integer pointsUsed = request.getPointsUsed() != null ? request.getPointsUsed() : 0;
        Client client = clientService.existUserByUserId(userId);
        if (client != null && pointsUsed > 0) {
            if (client.getPoints() < pointsUsed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "积分不足");
            }
            client.setPoints(client.getPoints() - pointsUsed);
            clientService.updateClient(client);
        }
        double actualPay = Math.max(totalAmount - pointsUsed / 10.0, 0);

        LocalDate now = LocalDate.now();

        ProductOrder order = new ProductOrder();
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setPointsUsed(pointsUsed);
        order.setPointsReward(totalPointsReward);
        order.setActualPay(actualPay);
        order.setOrderDate(now.format(DateTimeFormatter.ISO_LOCAL_DATE));
        order.setStatus(ProductOrderStatus.PAID);
        order.setStatusText("已付款");

        for (ClientOrderRequest.OrderItemRequest itemReq : items) {
            Product product = productService.getProductById((long) itemReq.getProductId()).get();
            if (product.getStock() != null) {
                productService.updateStock((long) itemReq.getProductId(),
                        product.getStock() - itemReq.getQuantity());
            }
        }

        if (client != null && totalPointsReward > 0) {
            client.setPoints(client.getPoints() + totalPointsReward);
            clientService.updateClient(client);
        }

        return productOrderService.createOrder(order);
    }

    private Integer getCurrentClientId(String token) {
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
