package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.CheckinTicket;
import org.example.fitnessjava.pojo.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckinTicketRepository extends JpaRepository<CheckinTicket, Integer> {
    
    List<CheckinTicket> findByStatus(TicketStatus status);
    
    List<CheckinTicket> findByMemberId(Integer memberId);
    
    @Query("SELECT ct FROM CheckinTicket ct WHERE " +
           "(:status IS NULL OR ct.status = :status) AND " +
           "(:memberId IS NULL OR ct.memberId = :memberId)")
    List<CheckinTicket> findCheckinTickets(@Param("status") TicketStatus status,
                                           @Param("memberId") Integer memberId);

    Optional<CheckinTicket> findByQrCode(String qrCode);

    @Query("SELECT ct FROM CheckinTicket ct WHERE ct.memberId = :memberId AND ct.status = :status ORDER BY ct.scheduledTime ASC LIMIT 1")
    Optional<CheckinTicket> findFirstByMemberIdAndStatusOrderByScheduledTimeAsc(@Param("memberId") Integer memberId, @Param("status") TicketStatus status);
}
