package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.CoachLessonSettlement;

import java.util.List;

public interface CoachLessonSettlementService {
    void settleForBooking(Booking booking);

    List<CoachLessonSettlement> getByCoachId(Integer coachId);
}
