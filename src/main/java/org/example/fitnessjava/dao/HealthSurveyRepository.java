package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.HealthSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthSurveyRepository extends JpaRepository<HealthSurvey, Long> {
    HealthSurvey findByUserId(Integer userId);
}