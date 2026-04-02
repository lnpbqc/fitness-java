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

    @Override
    public HealthSurvey getHealthSurveyById(String id) {
        return healthSurveyRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public HealthSurvey saveHealthSurvey(HealthSurvey healthSurvey) {
        return healthSurveyRepository.save(healthSurvey);
    }

    @Override
    public HealthSurvey deleteHealthSurveyById(String id) {
        return healthSurveyRepository.deleteById(Integer.valueOf(id)).orElse(null);
    }

    @Override
    public HealthSurvey updateHealthSurvey(HealthSurvey healthSurvey) {
        HealthSurvey res =  healthSurveyRepository.findById(Long.valueOf(healthSurvey.getId())).orElse(null);
        if (res != null) {
            return healthSurveyRepository.save(healthSurvey);
        }
        return null;
    }
}
