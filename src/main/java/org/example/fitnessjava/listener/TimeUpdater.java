package org.example.fitnessjava.listener;


import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BookingCoachScheduleSlotRepository;
import org.example.fitnessjava.dao.CheckinTicketRepository;
import org.example.fitnessjava.dao.CoachScheduleSlotRepository;
import org.example.fitnessjava.pojo.CoachScheduleSlot;
import org.example.fitnessjava.pojo.TicketStatus;
import org.example.fitnessjava.service.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeUpdater { // 定时任务

    @Resource
    private CheckinTicketRepository checkinTicketRepository;

    @Resource
    private CoachScheduleSlotRepository coachScheduleSlotRepository;

    @Resource
    private BookingCoachScheduleSlotRepository bookingCoachScheduleSlotRepository;

    @Resource
    private BookingService bookingService;

    @Scheduled(cron = "0 0 12 * * ? ")
    private void UpdateCheckin(){
        // 预约取消  核销更新  coachScheduleSlot更新
        checkinTicketRepository.findAll().forEach(checkinTicket -> {
            if(checkinTicket.getStatus()== TicketStatus.UNUSED){
                CoachScheduleSlot scheduleSlot = coachScheduleSlotRepository.findById(bookingCoachScheduleSlotRepository.findByBookingId(checkinTicket.getBookingId()).getCoachScheduleSlotId()).orElse(null);
                if(scheduleSlot==null){
                    return;
                }
                if(scheduleSlot.getEndTime()==null||scheduleSlot.getEndTime().isEmpty()){
                    return;
                }
                if(LocalDate.now().isAfter(LocalDate.parse(scheduleSlot.getEndTime()))){
                    checkinTicket.setStatus(TicketStatus.EXPIRED);  // 过期
                    checkinTicketRepository.save(checkinTicket);
                    bookingService.cancelBooking(checkinTicket.getBookingId(),"时间过期");
                    scheduleSlot.setAvailable(false);
                }
            }
        });
    }
}
