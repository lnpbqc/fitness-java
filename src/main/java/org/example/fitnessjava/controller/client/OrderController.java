package org.example.fitnessjava.controller.client;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/client/orders")
@Slf4j
@Tag(name = "客户端商品接口", description = "客户端商品、分类和商品详情查询接口")
public class OrderController {
    @PostMapping
    public Boolean postOrder(@RequestBody HashMap<String,Object> order,@RequestHeader("Authorization") String token) {
        log.info("order: {}", order);
        log.info("token: {}", token);// 没有就报错参数非法
        // 套餐请求数据:
        // order: {totalAmount=3999, pointsUsed=0, packageId=2, type=PACKAGE}
        // 商品请求数据:
        // order: {totalAmount=358, pointsUsed=0, type=SHOP, items=[{productId=1, quantity=1}, {productId=2, quantity=1}]}
        return true;
    }
}
