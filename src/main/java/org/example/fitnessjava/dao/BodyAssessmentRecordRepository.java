package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.BodyAssessmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyAssessmentRecordRepository extends JpaRepository<BodyAssessmentRecord, Integer> {
    List<BodyAssessmentRecord> findByUserId(Integer userId);
    Optional<BodyAssessmentRecord> findByIdAndUserId(Integer id, Integer userId);
    List<BodyAssessmentRecord> findByUserIdAndCoachId(Integer userId, Integer coachId);
}
