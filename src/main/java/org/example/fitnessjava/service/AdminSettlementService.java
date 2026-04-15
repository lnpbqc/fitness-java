package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.vo.AdminSettlementListItemVO;
import org.example.fitnessjava.pojo.vo.AdminSettlementListResponseVO;
import org.example.fitnessjava.pojo.vo.AdminSettlementSummaryVO;

public interface AdminSettlementService {

    AdminSettlementListResponseVO getSettlements(String startDate,
                                                 String endDate,
                                                 Integer coachId,
                                                 Integer bookingId,
                                                 Integer page,
                                                 Integer pageSize);

    AdminSettlementSummaryVO getSummary(String startDate,
                                        String endDate,
                                        Integer coachId,
                                        Integer bookingId);

    AdminSettlementListItemVO getDetail(Long id);
}
