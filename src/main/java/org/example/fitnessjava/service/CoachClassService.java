package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.CoachClass;
import org.example.fitnessjava.pojo.CoachClassStatus;
import org.example.fitnessjava.pojo.vo.CoachClassVO;

import java.util.List;
import java.util.Optional;

public interface CoachClassService {
    
    List<CoachClass> getAllCoachClasses();
    
    List<CoachClassVO> getAllCoachClassesWithInfo();
    
    List<CoachClass> getCoachClassesByCoachId(Integer coachId);
    
    List<CoachClass> getCoachClassesByStatus(CoachClassStatus status);
    
    List<CoachClassVO> getCoachClassesWithInfo(Integer coachId, CoachClassStatus status, Integer clientId);
    
    Optional<CoachClass> getCoachClassById(Integer id);
    
    CoachClassVO getCoachClassByIdWithInfo(Integer id);
    
    CoachClass createCoachClass(CoachClass coachClass);
    
    CoachClass updateCoachClass(CoachClass coachClass);
    
    CoachClass updateCoachClassStatus(Integer id, CoachClassStatus status);
    
    void deleteCoachClass(Integer id);
}
