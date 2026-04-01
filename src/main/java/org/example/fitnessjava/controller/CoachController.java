package org.example.fitnessjava.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "教练端接口", description = "教练登录、教练信息和教练列表查询接口")
public class CoachController {

    @Resource
    private CoachService coachService;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private WxMaService wxMaService;

    @GetMapping("/recommended")
    @Operation(summary = "获取推荐教练", description = "返回推荐教练列表，主要用于首页或推荐位展示")
    @Cacheable(value = "coaches",key = "'recommend'")
    public ArrayList<Coach> recommendedCoaches() {
        return coachService.getCoachesByFeatured();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取教练详情", description = "根据教练id查询单个教练详情")
    @Cacheable(value = "coach",key = "#id")
    public Coach coaches(
            @Parameter(description = "教练ID", example = "1")
            @PathVariable int id
    ) {
        Optional<Coach> coachById = coachService.getCoachById((long) id);
        return coachById.orElse(null);
    }


    @GetMapping()
    @Operation(summary = "按标签获取教练列表", description = "支持 all、today、mine 三种标签；mine 需要携带 Authorization")
    @Cacheable(value = "coaches", key = "(#tabType != null ? #tabType : 'all') + ':' + (#token != null ? #token : 'anonymous')")
    public ArrayList<Coach> coaches(
            @Parameter(description = "教练列表标签，可选值：all、today、mine", example = "all")
            @RequestParam(required = false, defaultValue = "all") String tabType,
            @Parameter(description = "用户登录 token，tabType=mine 时必传", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
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
    @Operation(summary = "教练小程序登录", description = "通过微信 code 登录，首次登录自动创建教练账号")
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
    @Operation(summary = "获取当前教练信息", description = "根据 Authorization 中的 token 获取当前教练信息")
    @Cacheable(value = "coach",key = "#token")
    public Coach getUserById(
            @Parameter(description = "教练登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            return new Coach();
        }
        Optional<Coach> coachByOpenid = coachService.getCoachByOpenid(openid);
        return coachByOpenid.orElse(new Coach());
    }
}
