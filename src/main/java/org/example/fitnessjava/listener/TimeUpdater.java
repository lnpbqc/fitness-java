package org.example.fitnessjava.listener;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BookingCoachScheduleSlotRepository;
import org.example.fitnessjava.dao.CheckinTicketRepository;
import org.example.fitnessjava.dao.CoachScheduleSlotRepository;
import org.example.fitnessjava.pojo.BookingCoachScheduleSlot;
import org.example.fitnessjava.pojo.CheckinTicket;
import org.example.fitnessjava.pojo.CoachScheduleSlot;
import org.example.fitnessjava.pojo.TicketStatus;
import org.example.fitnessjava.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class TimeUpdater {

    private static final Logger log = LoggerFactory.getLogger(TimeUpdater.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Resource
    private CheckinTicketRepository checkinTicketRepository;

    @Resource
    private CoachScheduleSlotRepository coachScheduleSlotRepository;

    @Resource
    private BookingCoachScheduleSlotRepository bookingCoachScheduleSlotRepository;

    @Resource
    private BookingService bookingService;

    @Scheduled(cron = "0 */10 * * * ?")
    @Transactional
    public void updateCheckinStatus() {
        List<CheckinTicket> tickets = checkinTicketRepository.findByStatus(TicketStatus.UNUSED);
        LocalDateTime now = LocalDateTime.now();

        for (CheckinTicket ticket : tickets) {
            BookingCoachScheduleSlot mapping = bookingCoachScheduleSlotRepository.findByBookingId(ticket.getBookingId());
            if (mapping == null) {
                log.warn("Skip ticket {}: booking-slot mapping not found", ticket.getBookingId());
                continue;
            }

            CoachScheduleSlot slot = coachScheduleSlotRepository.findById(mapping.getCoachScheduleSlotId()).orElse(null);
            if (slot == null) {
                log.warn("Skip ticket {}: schedule slot {} not found", ticket.getBookingId(), mapping.getCoachScheduleSlotId());
                continue;
            }

            if (isSlotExpired(slot, now)) {
                ticket.setStatus(TicketStatus.EXPIRED);
                checkinTicketRepository.save(ticket);
                try {
                    bookingService.cancelBooking(ticket.getBookingId(), "AUTO_EXPIRED");
                    coachScheduleSlotRepository.findById(mapping.getCoachScheduleSlotId()).ifPresent(expiredSlot -> {
                        expiredSlot.setAvailable(false);
                        coachScheduleSlotRepository.save(expiredSlot);
                    });
                } catch (Exception e) {
                    log.error("Auto cancel booking failed, bookingId={}", ticket.getBookingId(), e);
                    slot.setAvailable(false);
                    coachScheduleSlotRepository.save(slot);
                }
            }
        }
    }

    private boolean isSlotExpired(CoachScheduleSlot slot, LocalDateTime now) {
        if (isBlank(slot.getDate()) || isBlank(slot.getEndTime())) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(slot.getDate());
            LocalTime endTime = LocalTime.parse(slot.getEndTime(), TIME_FORMATTER);
            LocalDateTime endAt = LocalDateTime.of(date, endTime);
            return now.isAfter(endAt);
        } catch (DateTimeParseException e) {
            log.warn("Invalid slot date/time format, slotId={}, date={}, endTime={}",
                    slot.getId(), slot.getDate(), slot.getEndTime());
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
