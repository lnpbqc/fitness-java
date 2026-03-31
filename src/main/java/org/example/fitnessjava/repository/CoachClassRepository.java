package org.example.fitnessjava.repository;

import org.example.fitnessjava.pojo.penddingEntity.CoachClass;
import org.example.fitnessjava.pojo.penddingEntity.CoachClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachClassRepository extends JpaRepository<CoachClass, Integer> {
    
    List<CoachClass> findByCoachId(Integer coachId);
    
    List<CoachClass> findByClientId(Integer clientId);
    
    List<CoachClass> findByStatus(CoachClassStatus status);
    
    @Query("SELECT cc FROM CoachClass cc WHERE " +
           "(:coachId IS NULL OR cc.coachId = :coachId) AND " +
           "(:status IS NULL OR cc.status = :status) AND " +
           "(:clientId IS NULL OR cc.clientId = :clientId)")
    List<CoachClass> findCoachClasses(@Param("coachId") Integer coachId,
                                      @Param("status") CoachClassStatus status,
                                      @Param("clientId") Integer clientId);
}
