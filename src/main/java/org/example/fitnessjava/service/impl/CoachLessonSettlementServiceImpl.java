package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.CoachLessonSettlementRepository;
import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.CoachLessonSettlement;
import org.example.fitnessjava.pojo.vo.CoachCompensationSettingsVO;
import org.example.fitnessjava.service.CoachCompensationService;
import org.example.fitnessjava.service.CoachLessonSettlementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class CoachLessonSettlementServiceImpl implements CoachLessonSettlementService {

    @Resource
    private CoachLessonSettlementRepository coachLessonSettlementRepository;

    @Resource
    private CoachCompensationService coachCompensationService;

    @Override
    @Transactional
    public void settleForBooking(Booking booking) {
        if (booking == null || booking.getId() <= 0) {
            return;
        }

        if (coachLessonSettlementRepository.findByBookingId(booking.getId()).isPresent()) {
            return;
        }

        CoachCompensationSettingsVO settings = coachCompensationService.getSettings();
        double lessonUnitPrice = nvl(settings.getLessonUnitPrice());
        double coachRatio = nvl(settings.getCoachRatio());
        double coachFee = round(lessonUnitPrice * coachRatio);
        double bossFee = round(lessonUnitPrice - coachFee);

        CoachLessonSettlement settlement = new CoachLessonSettlement();
        settlement.setBookingId(booking.getId());
        settlement.setCoachId(booking.getCoachId());
        settlement.setLessonUnitPrice(round(lessonUnitPrice));
        settlement.setCoachRatio(round(coachRatio));
        settlement.setCoachFee(coachFee);
        settlement.setBossFee(bossFee);
        settlement.setSettledDate(booking.getBookingDate() == null || booking.getBookingDate().isBlank()
                ? LocalDate.now().toString()
                : booking.getBookingDate());
        coachLessonSettlementRepository.save(settlement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachLessonSettlement> getByCoachId(Integer coachId) {
        return coachLessonSettlementRepository.findByCoachId(coachId);
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private double nvl(Double value) {
        return value == null ? 0 : value;
    }
}
