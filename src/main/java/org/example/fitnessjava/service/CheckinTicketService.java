package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.CheckinTicket;
import org.example.fitnessjava.pojo.TicketStatus;
import org.example.fitnessjava.pojo.vo.CheckinTicketVO;

import java.util.List;
import java.util.Optional;

public interface CheckinTicketService {
    
    List<CheckinTicket> getAllCheckinTickets();
    
    List<CheckinTicketVO> getAllCheckinTicketsWithInfo();
    
    List<CheckinTicket> getCheckinTicketsByStatus(TicketStatus status);
    
    List<CheckinTicketVO> getCheckinTicketsWithInfo(TicketStatus status, Integer memberId);
    
    Optional<CheckinTicket> getCheckinTicketById(Integer bookingId);
    
    CheckinTicketVO getCheckinTicketByIdWithInfo(Integer bookingId);
    
    CheckinTicket createCheckinTicket(CheckinTicket checkinTicket);
    
    CheckinTicket updateCheckinTicketStatus(Integer bookingId, TicketStatus status);
    
    void deleteCheckinTicket(Integer bookingId);
}
