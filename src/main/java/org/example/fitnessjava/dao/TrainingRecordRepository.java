package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.TrainingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Integer> {
    List<TrainingRecord> findByClientId(Integer clientId);
    List<TrainingRecord> findByClientIdAndCoachId(Integer clientId, Integer coachId);
    List<TrainingRecord> findByCoachId(Integer coachId);
}
