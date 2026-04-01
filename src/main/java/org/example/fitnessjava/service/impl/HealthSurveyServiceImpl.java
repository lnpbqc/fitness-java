package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.HealthSurveyRepository;
import org.example.fitnessjava.pojo.HealthSurvey;
import org.example.fitnessjava.service.HealthSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthSurveyServiceImpl implements HealthSurveyService {
    @Resource
    private HealthSurveyRepository healthSurveyRepository;

    @Override
    public HealthSurvey getHealthSurveyByUserId(String userId) {
        healthSurveyRepository.findByUserId(Integer.valueOf(userId));
        return null;
    }
}
