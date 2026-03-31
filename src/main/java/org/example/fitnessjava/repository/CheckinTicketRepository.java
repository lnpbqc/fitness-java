package org.example.fitnessjava.repository;

import org.example.fitnessjava.pojo.penddingEntity.CheckinTicket;
import org.example.fitnessjava.pojo.penddingEntity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckinTicketRepository extends JpaRepository<CheckinTicket, Integer> {
    
    List<CheckinTicket> findByStatus(TicketStatus status);
    
    List<CheckinTicket> findByMemberId(Integer memberId);
    
    @Query("SELECT ct FROM CheckinTicket ct WHERE " +
           "(:status IS NULL OR ct.status = :status) AND " +
           "(:memberId IS NULL OR ct.memberId = :memberId)")
    List<CheckinTicket> findCheckinTickets(@Param("status") TicketStatus status,
                                           @Param("memberId") Integer memberId);
}
