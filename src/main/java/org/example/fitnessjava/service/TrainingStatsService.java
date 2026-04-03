package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.BodyAssessmentRecord;
import org.example.fitnessjava.pojo.vo.TrainingStatsResponse;

import java.util.List;
import java.util.Optional;

public interface TrainingStatsService {
    TrainingStatsResponse getTrainingStats(Integer userId);
    List<BodyAssessmentRecord> getRecordsByUserId(Integer userId);
    Optional<BodyAssessmentRecord> getRecordById(Integer userId, Integer id);
    BodyAssessmentRecord createRecord(Integer userId, BodyAssessmentRecord record);
    Optional<BodyAssessmentRecord> updateRecord(Integer userId, Integer id, BodyAssessmentRecord record);
    void deleteRecord(Integer userId, Integer id);
}
