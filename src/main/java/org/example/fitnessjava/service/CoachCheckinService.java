package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.vo.CoachCheckinResponse;

public interface CoachCheckinService {
    CoachCheckinResponse scanAndCheckin(Integer coachId, String qrCode);
}
