package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.CheckinTicket;
import org.example.fitnessjava.pojo.TicketStatus;
import org.example.fitnessjava.pojo.vo.CheckinTicketVO;
import org.example.fitnessjava.dao.CheckinTicketRepository;
import org.example.fitnessjava.service.CheckinTicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckinTicketServiceImpl implements CheckinTicketService {

    @Resource
    private CheckinTicketRepository checkinTicketRepository;

    private CheckinTicketVO convertToVO(CheckinTicket ticket) {
        CheckinTicketVO vo = new CheckinTicketVO();
        vo.setBookingId(ticket.getBookingId());
        vo.setQrCode(ticket.getQrCode());
        vo.setMemberId(ticket.getMemberId());
        vo.setMemberName(ticket.getMemberName());
        vo.setMemberAvatar(ticket.getMemberAvatar());
        vo.setClassType(ticket.getClassType());
        vo.setScheduledTime(ticket.getScheduledTime());
        vo.setSessionsLeft(ticket.getSessionsLeft());
        vo.setStatus(ticket.getStatus());
        vo.setStatusText(getStatusText(ticket.getStatus()));
        return vo;
    }

    private String getStatusText(TicketStatus status) {
        switch (status) {
            case UNUSED: return "未使用";
            case USED: return "已使用";
            case EXPIRED: return "已过期";
            default: return "未知";
        }
    }

    @Override
    public List<CheckinTicket> getAllCheckinTickets() {
        return checkinTicketRepository.findAll();
    }

    @Override
    public List<CheckinTicketVO> getAllCheckinTicketsWithInfo() {
        return checkinTicketRepository.findAll().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckinTicket> getCheckinTicketsByStatus(TicketStatus status) {
        return checkinTicketRepository.findByStatus(status);
    }

    @Override
    public List<CheckinTicketVO> getCheckinTicketsWithInfo(TicketStatus status, Integer memberId) {
        return checkinTicketRepository.findCheckinTickets(status, memberId).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CheckinTicket> getCheckinTicketById(Integer bookingId) {
        return checkinTicketRepository.findById(bookingId);
    }

    @Override
    public CheckinTicketVO getCheckinTicketByIdWithInfo(Integer bookingId) {
        return checkinTicketRepository.findById(bookingId)
                .map(this::convertToVO)
                .orElse(null);
    }

    @Override
    @Transactional
    public CheckinTicket createCheckinTicket(CheckinTicket checkinTicket) {
        return checkinTicketRepository.save(checkinTicket);
    }

    @Override
    @Transactional
    public CheckinTicket updateCheckinTicketStatus(Integer bookingId, TicketStatus status) {
        return checkinTicketRepository.findById(bookingId)
                .map(ticket -> {
                    ticket.setStatus(status);
                    return checkinTicketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("核销券不存在"));
    }

    @Override
    @Transactional
    public void deleteCheckinTicket(Integer bookingId) {
        checkinTicketRepository.deleteById(bookingId);
    }
}
