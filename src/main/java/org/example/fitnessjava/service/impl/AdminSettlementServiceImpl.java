package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BookingRepository;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CoachLessonSettlementRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.CoachLessonSettlement;
import org.example.fitnessjava.pojo.vo.AdminSettlementListItemVO;
import org.example.fitnessjava.pojo.vo.AdminSettlementListResponseVO;
import org.example.fitnessjava.pojo.vo.AdminSettlementSummaryVO;
import org.example.fitnessjava.service.AdminSettlementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AdminSettlementServiceImpl implements AdminSettlementService {

    @Resource
    private CoachLessonSettlementRepository settlementRepository;

    @Resource
    private CoachRepository coachRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminSettlementListResponseVO getSettlements(String startDate,
                                                        String endDate,
                                                        Integer coachId,
                                                        Integer bookingId,
                                                        Integer page,
                                                        Integer pageSize) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safePageSize = pageSize == null || pageSize < 1 ? 20 : Math.min(pageSize, 200);

        List<CoachLessonSettlement> all = settlementRepository.findForAdmin(
                blankToNull(startDate),
                blankToNull(endDate),
                coachId,
                bookingId
        );

        List<AdminSettlementListItemVO> mapped = new ArrayList<>();
        for (CoachLessonSettlement settlement : all) {
            mapped.add(toItemVO(settlement));
        }

        int fromIndex = (safePage - 1) * safePageSize;
        List<AdminSettlementListItemVO> pageItems;
        if (fromIndex >= mapped.size()) {
            pageItems = Collections.emptyList();
        } else {
            int toIndex = Math.min(fromIndex + safePageSize, mapped.size());
            pageItems = mapped.subList(fromIndex, toIndex);
        }

        AdminSettlementListResponseVO response = new AdminSettlementListResponseVO();
        response.setItems(pageItems);
        response.setTotal((long) mapped.size());
        response.setPage(safePage);
        response.setPageSize(safePageSize);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSettlementSummaryVO getSummary(String startDate,
                                               String endDate,
                                               Integer coachId,
                                               Integer bookingId) {
        List<CoachLessonSettlement> all = settlementRepository.findForAdmin(
                blankToNull(startDate),
                blankToNull(endDate),
                coachId,
                bookingId
        );

        double totalLessonAmount = 0;
        double totalCoachFee = 0;
        double totalBossFee = 0;
        for (CoachLessonSettlement settlement : all) {
            double lessonAmount = nvl(settlement.getLessonUnitPrice());
            totalLessonAmount += lessonAmount;
            totalCoachFee += nvl(settlement.getCoachFee());
            totalBossFee += nvl(settlement.getBossFee());
        }

        AdminSettlementSummaryVO summary = new AdminSettlementSummaryVO();
        summary.setTotalCount((long) all.size());
        summary.setTotalLessonAmount(round(totalLessonAmount));
        summary.setTotalCoachFee(round(totalCoachFee));
        summary.setTotalBossFee(round(totalBossFee));
        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSettlementListItemVO getDetail(Long id) {
        CoachLessonSettlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("结算流水不存在"));
        return toItemVO(settlement);
    }

    private AdminSettlementListItemVO toItemVO(CoachLessonSettlement settlement) {
        AdminSettlementListItemVO item = new AdminSettlementListItemVO();
        item.setId(settlement.getId());
        item.setBookingId(settlement.getBookingId());
        item.setCoachId(settlement.getCoachId());
        item.setSettledDate(settlement.getSettledDate());
        item.setLessonUnitPrice(round(nvl(settlement.getLessonUnitPrice())));
        item.setCoachRatio(round(nvl(settlement.getCoachRatio())));
        item.setCoachFee(round(nvl(settlement.getCoachFee())));
        item.setBossFee(round(nvl(settlement.getBossFee())));
        item.setLessonAmount(round(nvl(settlement.getLessonUnitPrice())));
        item.setCreateTime(settlement.getCreateTime());

        if (settlement.getCoachId() != null) {
            Optional<Coach> coachOpt = coachRepository.findById(Long.valueOf(settlement.getCoachId()));
            coachOpt.ifPresent(coach -> item.setCoachName(coach.getNickname()));
        }

        if (settlement.getBookingId() != null) {
            Optional<Booking> bookingOpt = bookingRepository.findById(settlement.getBookingId());
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                item.setMemberId(booking.getUserId());
                Optional<Client> clientOpt = clientRepository.findById(Long.valueOf(booking.getUserId()));
                clientOpt.ifPresent(client -> item.setMemberName(client.getNickname()));
            }
        }

        return item;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private double nvl(Double value) {
        return value == null ? 0 : value;
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
