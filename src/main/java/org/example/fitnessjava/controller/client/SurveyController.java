package org.example.fitnessjava.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.HealthSurvey;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.HealthSurveyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/survey")
@Tag(name = "问卷接口", description = "问卷增删改查接口,需保证请求头的Authorization与问卷所属用户一致")
public class SurveyController {
    @Resource
    private HealthSurveyService healthSurveyService;
    @Resource
    private ClientService clientService;

    @GetMapping("/{id}")
    @Operation(description = "根据id获取问卷")
    public HealthSurvey getSurveyById(@PathVariable("id") String id,@RequestHeader("Authorization") String token) {
        int clientServiceIdByToken = clientService.getIdByToken(token);
        HealthSurvey healthSurveyById = healthSurveyService.getHealthSurveyById(id);
        if (healthSurveyById.getUserId()!=clientServiceIdByToken) {
            return new HealthSurvey();
        }else{
            return healthSurveyById;
        }
    }
    @PutMapping()
    @Operation(description = "传入HealthSurvey更新问卷")
    public HealthSurvey updateById(@RequestBody HealthSurvey healthSurvey,@RequestHeader("Authorization") String token) {
        int clientServiceIdByToken = clientService.getIdByToken(token);
        if (healthSurvey.getUserId()!=clientServiceIdByToken) {
            return new HealthSurvey();
        }else{
            return healthSurveyService.updateHealthSurvey(healthSurvey);
        }
    }
    @PostMapping
    @Operation(description = "传入HealthSurvey创建问卷")
    public HealthSurvey createSurvey(@RequestBody HealthSurvey healthSurvey,@RequestHeader("Authorization") String token){
        int clientServiceIdByToken = clientService.getIdByToken(token);
        healthSurvey.setUserId(clientServiceIdByToken);
        return healthSurveyService.saveHealthSurvey(healthSurvey);
    }
    @DeleteMapping("/{id}")
    @Operation(description = "根据id删除问卷")
    public HealthSurvey deleteById(@PathVariable("id") String id,@RequestHeader("Authorization") String token){
        int clientServiceIdByToken = clientService.getIdByToken(token);
        HealthSurvey healthSurveyById = healthSurveyService.getHealthSurveyById(id);
        if (healthSurveyById.getUserId()!=clientServiceIdByToken) {
            return new HealthSurvey();
        }
        return healthSurveyService.deleteHealthSurveyById(id);
    }
}
