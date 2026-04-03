package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.vo.CoachPerformanceSummaryVO;

import java.util.List;
import java.util.Map;

public interface CoachPerformanceService {
    CoachPerformanceSummaryVO getPerformanceSummary(Integer coachId);
    List<Map<String, Object>> getPerformanceTrend(Integer coachId);
}
