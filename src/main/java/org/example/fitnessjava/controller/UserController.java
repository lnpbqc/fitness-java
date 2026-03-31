package org.example.fitnessjava.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.example.fitnessjava.config.WXAConfig;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.dto.WxLoginRequest;
import org.example.fitnessjava.pojo.vo.UserVO;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/client/user")
@Slf4j
public class UserController {
    @Resource
    private ClientService clientService;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private WxMaService wxMaService;

    @PostMapping("/login")
    @Operation(description = "小程序登录")
    public Map<String, String> login(@RequestBody WxLoginRequest req) {

        Map<String, String> res = new HashMap<>();
        log.info(req.toString());

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
            String sessionKey = result.getSessionKey();

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
    @Operation(description = "通过携带的token获取对应id")
    @Cacheable(value = "user",key = "#token")
    public UserVO getUserById(@RequestHeader("Authorization") String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            return null;
        }
        Client client = clientService.existUser(openid);
        if (client == null) {
            return null;
        }
        return clientService.convertToUserVO(client);
    }
}
