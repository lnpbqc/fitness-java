package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.HealthSurvey;

public interface HealthSurveyService {
    HealthSurvey getHealthSurveyByUserId(String userId);
    HealthSurvey getHealthSurveyById(String id);
    HealthSurvey saveHealthSurvey(HealthSurvey healthSurvey);
    HealthSurvey deleteHealthSurveyById(String id);
    HealthSurvey updateHealthSurvey(HealthSurvey healthSurvey);
}
