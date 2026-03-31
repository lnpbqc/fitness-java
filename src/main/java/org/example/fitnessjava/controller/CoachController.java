package org.example.fitnessjava.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.dto.WxLoginRequest;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/coach")
@Slf4j
public class CoachController {

    @Resource
    private CoachService coachService;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private WxMaService wxMaService;

    @GetMapping("/recommended")
    @Operation(summary = "获取推荐教练")
    @Cacheable(value = "coaches",key = "'recommend'")
    public ArrayList<Coach> recommendedCoaches() {
        return coachService.getCoachesByFeatured();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取具体教练")
    @Cacheable(value = "coach",key = "#id")
    public Coach coaches(@PathVariable int id) {
        Optional<Coach> coachById = coachService.getCoachById((long) id);
        return coachById.orElse(null);
    }


    @GetMapping()
    @Operation(summary = "获取指定类型教练,all|today|mine")
    @Cacheable(value = "coaches", key = "(#tabType != null ? #tabType : 'all') + ':' + (#token != null ? #token : 'anonymous')")
    public ArrayList<Coach> coaches(
            @RequestParam(required = false, defaultValue = "all") String tabType,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        String normalizedTabType = Optional.ofNullable(tabType)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(value -> !value.isEmpty())
                .orElse("all");
        log.info("CoachController: coaches tabType: {}", normalizedTabType);
        switch (normalizedTabType) {
            case "today":
                return coachService.getTodayCoaches();
            case "mine":
                String openid = jwtUtil.getSubjectFromAuthorization(token);
                if (openid == null) {
                    log.warn("CoachController: 无效的 Authorization token");
                    return new ArrayList<>();
                }
                return coachService.getCoachesOfUser(openid);
            case "all":
            default:
                return coachService.getAllCoaches();
        }
    }

    @PostMapping("/login")
    @Operation(description = "教练小程序登录")
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

            Optional<Coach> optional = coachService.getCoachByOpenid(openid);

            if (optional.isEmpty()) {
                Coach coach = new Coach();
                coach.setNickname(req.getNickname());
                coach.setAvatar(req.getAvatar());
                coach.setOpenid(openid);
                coachService.createCoach(coach);
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
    @Cacheable(value = "coach",key = "#token")
    public Coach getUserById(@RequestHeader("Authorization") String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            return new Coach();
        }
        Optional<Coach> coachByOpenid = coachService.getCoachByOpenid(openid);
        return coachByOpenid.orElse(new Coach());
    }



}
