package org.example.fitnessjava.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Banner;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.HealthSurvey;
import org.example.fitnessjava.pojo.vo.ClientHomeResponse;
import org.example.fitnessjava.service.BannerService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.service.HealthSurveyService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/client/home")
@Tag(name = "客户端首页接口", description = "客户端首页聚合数据查询接口")
public class HomeController {
    @Resource
    HealthSurveyService healthSurveyService;
    @Resource
    CoachService coachService;
    @Resource
    BannerService bannerService;

    @GetMapping
    @Operation(summary = "获取首页信息", description = "返回轮播图、推荐教练和健康问卷信息")
    @Cacheable(key = "#userId",value = "CHR")
    public ClientHomeResponse home(
            @Parameter(description = "用户ID，未登录时传 -1 或不传", example = "-1")
            @RequestParam(defaultValue = "-1") String userId
    ) {
        HealthSurvey survey = null;
        if (userId == null || userId.isEmpty() || userId.equals("-1")) {
            survey = new HealthSurvey();
        }else{
            try {
                survey = healthSurveyService.getHealthSurveyByUserId(userId);
            }catch (Exception e) {
                throw new IllegalArgumentException("非法用户id");
            }
        }
        ArrayList<Coach> coachesByFeatures = coachService.getCoachesByFeatured();
        coachesByFeatures.forEach(coach -> coach.setPosterUrl(null));
        ArrayList<Banner> banners = bannerService.getBanners();

        ClientHomeResponse response = new ClientHomeResponse();
        response.setFeaturedCoaches(coachesByFeatures);
        response.setBanners(banners);
        response.setSurvey(survey);
        return response;
    }
}


