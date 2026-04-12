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

    private String clientAppId;
    private String clientAppSecret;
    private String coachAppId;
    private String coachAppSecret;

    @Bean("coachWXMaService")
    public WxMaService coachWXMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(coachAppId);
        config.setSecret(coachAppSecret);
        log.info("Init coach miniapp config, appId={}, secretMask={}",
            mask(coachAppId), mask(coachAppSecret));

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

    @Bean("clientWXMaService")
    public WxMaService clientWXMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(clientAppId);
        config.setSecret(clientAppSecret);
        log.info("Init client miniapp config, appId={}, secretMask={}",
            mask(clientAppId), mask(clientAppSecret));

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

    private String mask(String source) {
        if (source == null || source.isBlank()) {
            return "<empty>";
        }
        int keep = Math.min(4, source.length());
        if (source.length() <= keep * 2) {
            return "****";
        }
        return source.substring(0, keep) + "****" + source.substring(source.length() - keep);
    }
}
