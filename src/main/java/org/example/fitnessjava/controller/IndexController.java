package org.example.fitnessjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
@Tag(name = "系统测试接口", description = "项目基础连通性和健康检查用接口")
public class IndexController {

    @GetMapping
    @Operation(summary = "连通性测试", description = "返回固定字符串，用于测试服务是否启动成功")
    public String get(){
        return "hello world";
    }
}
