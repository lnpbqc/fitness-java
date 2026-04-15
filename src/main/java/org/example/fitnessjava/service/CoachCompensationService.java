package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.vo.CoachCompensationSettingsVO;

public interface CoachCompensationService {
    CoachCompensationSettingsVO getSettings();

    CoachCompensationSettingsVO updateSettings(Double lessonUnitPrice, Double coachRatio, String updatedBy);

    Double estimateCoachFeePerClass();
}
