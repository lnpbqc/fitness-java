package org.example.fitnessjava.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.dto.WxLoginRequest;
import org.example.fitnessjava.pojo.vo.UserVO;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/client/user")
@Slf4j
@Tag(name = "客户端用户接口", description = "客户端用户登录与当前用户信息查询接口")
public class UserController {
    @Resource
    private ClientService clientService;
    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("clientWXMaService")
    private WxMaService wxMaService;

    @PostMapping("/login")
    @Operation(summary = "客户端小程序登录", description = "通过微信 code 登录，首次登录自动创建客户端用户")
    public Map<String, String> login(@RequestBody WxLoginRequest req) {

        Map<String, String> res = new HashMap<>();
        log.info("Client miniapp login request received.");

        if (req.getCode() == null || req.getCode().isEmpty()) {
            res.put("error", "code is empty");
            return res;
        }

        try {
            WxMaJscode2SessionResult result =
                    wxMaService.getUserService().getSessionInfo(req.getCode());

            if (result == null) {
                res.put("error", "wx result null");
                return res;
            }

            String openid = result.getOpenid();

            if (openid == null) {
                res.put("error", "openid null");
                return res;
            }

            Client client = clientService.existUser(openid);

            if (client == null) {
                client = new Client();
                client.setNickname(req.getNickname());
                client.setAvatar(req.getAvatar());
                client.setOpenid(openid);
                clientService.addUser(client);
            }

            String token = jwtUtil.generateToken(openid, "user");

            res.put("token", token);
            res.put("openid", openid);

            return res;

        } catch (WxErrorException e) {
            log.error("微信登录失败: {}", e.getMessage(), e);
            res.put("error", e.getMessage());
            return res;
        }
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前客户端用户信息", description = "根据 Authorization 中的 token 获取当前客户端用户信息")
    @Cacheable(value = "user",key = "#token")
    public UserVO getUserById(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        Client client = clientService.existUser(openid);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is not bound to any client account");
        }
        return clientService.convertToUserVO(client);
    }
}
