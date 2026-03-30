package org.example.fitnessjava.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Banner;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.HealthSurvey;
import org.example.fitnessjava.pojo.vo.ClientHomeResponse;
import org.example.fitnessjava.service.BannerService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.service.HealthSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/client/home")
public class HomeController {
    @Resource
    HealthSurveyService healthSurveyService;
    @Resource
    CoachService coachService;
    @Resource
    BannerService bannerService;

    @GetMapping
    @Operation(summary = "获取首页信息")
    @Cacheable(key = "#userId",value = "CHR")
    public ClientHomeResponse home(@RequestParam(defaultValue = "-1") String userId) {
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
        ArrayList<Banner> banners = bannerService.getBanners();

        ClientHomeResponse response = new ClientHomeResponse();
        response.setFeaturedCoaches(coachesByFeatures);
        response.setBanners(banners);
        response.setSurvey(survey);
        return response;
    }
}


