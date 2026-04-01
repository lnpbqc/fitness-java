package org.example.fitnessjava.controller.client;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/client")
@Slf4j
@Tag(name = "客户端商品接口", description = "客户端商品、分类和商品详情查询接口")
public class OrderController {
    @PostMapping("/orders")
    public Boolean postOrder(@RequestBody HashMap<String,String> order) {
        log.info("order: {}", order);
        return true;
    }
}
