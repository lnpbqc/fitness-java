package org.example.fitnessjava.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import org.example.fitnessjava.config.WXAConfig;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.service.ClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/client/user")
public class UserController {
    @Resource
    private WXAConfig wxaConfig;
    @Resource
    private ClientService clientService;

    @PostMapping
    @Operation(summary = "通过code获取openid然后注册用户")
    public HashMap<String,String> login(@RequestBody final String code, @RequestBody final String nickname, @RequestBody final String avatar) throws WxErrorException {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxaConfig.appId);
        config.setSecret(wxaConfig.appSecret);
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);
        WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo("js-code");

        String openid = result.getOpenid();
        System.out.println(openid);

        Client client = clientService.existUser(openid);
        if (client == null) {
            client = new Client();
            client.setNickname(nickname);
            client.setAvatar(avatar);
            client.setOpenid(openid);
            clientService.addUser(client);
        }

        // todo: 加入SpringSecurity生成token
        return new HashMap<>(1);
    }
}
