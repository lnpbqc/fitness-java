package org.example.fitnessjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.service.CoachService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/coach")
@Slf4j
public class CoachController {

    @Resource
    private CoachService coachService;

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
    @Cacheable(value = "coaches", key = "#tabType + ':' + (#userId != null ? #userId : 'anonymous')")
    public ArrayList<Coach> coaches(
            @RequestParam(required = true,defaultValue = "all") String tabType,
            @RequestParam(required = false) String userId
    ) {
        log.info("CoachController: coaches tabType: {}, userId: {}", tabType, userId);
        switch (tabType) {
            case "all":
                return coachService.getAllCoaches();
            case "today":
                return coachService.getTodayCoaches();
            case "mine":
                return coachService.getCoachesOfUser(userId);
            default:
                return null;
        }
    }

}
