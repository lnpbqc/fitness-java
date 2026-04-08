package org.example.fitnessjava.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wx.miniapp")
@Data
@Slf4j
public class WXAConfig {
    public String clientAppId;
    public String clientAppSecret;
    public String coachAppId;
    public String coachAppSecret;
    @Bean("coachWXMaService")
    public WxMaService coachWXMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(coachAppId);
        config.setSecret(coachAppSecret);
        log.error("CoachAppId:{}", coachAppId);
        log.error("CoachAppSecret:{}", coachAppSecret);

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

    @Bean("clientWXMaService")
    public WxMaService clientWXMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(clientAppId);
        config.setSecret(clientAppSecret);
        log.error("ClientAppId:{}", clientAppId);
        log.error("ClientAppSecret:{}", clientAppSecret);

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }
}
