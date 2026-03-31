package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.repository.CoachClassRepository;
import org.example.fitnessjava.pojo.CoachClass;
import org.example.fitnessjava.pojo.CoachClassStatus;
import org.example.fitnessjava.pojo.vo.CoachClassVO;
import org.example.fitnessjava.service.CoachClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoachClassServiceImpl implements CoachClassService {

    @Resource
    private CoachClassRepository coachClassRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private CoachRepository coachRepository;

    private CoachClassVO convertToVO(CoachClass coachClass) {
        CoachClassVO vo = new CoachClassVO();
        vo.setId(coachClass.getId());
        vo.setCoachId(coachClass.getCoachId());
        vo.setClientId(coachClass.getClientId());
        vo.setDate(coachClass.getDate());
        vo.setStartTime(coachClass.getStartTime());
        vo.setEndTime(coachClass.getEndTime());
        vo.setType(coachClass.getType());
        vo.setRoomName(coachClass.getRoomName());
        vo.setNote(coachClass.getNote());
        vo.setTargetArea(coachClass.getTargetArea());
        vo.setEquipment(coachClass.getEquipment());
        vo.setSessionNumber(coachClass.getSessionNumber());
        vo.setTotalSessions(coachClass.getTotalSessions());
        vo.setMemberNote(coachClass.getMemberNote());
        vo.setStatus(coachClass.getStatus());
        vo.setStatusText(getStatusText(coachClass.getStatus()));

        // 获取教练名
        try {
            Coach coach = coachRepository.findById((long) coachClass.getCoachId()).orElse(null);
            if (coach != null) {
                vo.setCoachName(coach.getNickname());
            }
        } catch (Exception e) {
            vo.setCoachName("未知教练");
        }

        // 获取学员名
        try {
            Client client = clientRepository.findById((long) coachClass.getClientId()).orElse(null);
            if (client != null) {
                vo.setClientName(client.getNickname());
                vo.setClientAvatar(client.getAvatar());
            }
        } catch (Exception e) {
            vo.setClientName("未知学员");
        }

        return vo;
    }

    private String getStatusText(CoachClassStatus status) {
        switch (status) {
            case PENDING: return "待确认";
            case CONFIRMED: return "已确认";
            case COMPLETED: return "已完成";
            case RESCHEDULE_PENDING: return "待改期";
            default: return "未知";
        }
    }

    @Override
    public List<CoachClass> getAllCoachClasses() {
        return coachClassRepository.findAll();
    }

    @Override
    public List<CoachClassVO> getAllCoachClassesWithInfo() {
        return coachClassRepository.findAll().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoachClass> getCoachClassesByCoachId(Integer coachId) {
        return coachClassRepository.findByCoachId(coachId);
    }

    @Override
    public List<CoachClass> getCoachClassesByStatus(CoachClassStatus status) {
        return coachClassRepository.findByStatus(status);
    }

    @Override
    public List<CoachClassVO> getCoachClassesWithInfo(Integer coachId, CoachClassStatus status, Integer clientId) {
        return coachClassRepository.findCoachClasses(coachId, status, clientId).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CoachClass> getCoachClassById(Integer id) {
        return coachClassRepository.findById(id);
    }

    @Override
    public CoachClassVO getCoachClassByIdWithInfo(Integer id) {
        return coachClassRepository.findById(id)
                .map(this::convertToVO)
                .orElse(null);
    }

    @Override
    @Transactional
    public CoachClass createCoachClass(CoachClass coachClass) {
        if (coachClass.getStatus() == null) {
            coachClass.setStatus(CoachClassStatus.PENDING);
        }
        if (coachClass.getStatusText() == null) {
            coachClass.setStatusText("待确认");
        }
        return coachClassRepository.save(coachClass);
    }

    @Override
    @Transactional
    public CoachClass updateCoachClass(CoachClass coachClass) {
        return coachClassRepository.findById(coachClass.getId())
                .map(existing -> {
                    existing.setDate(coachClass.getDate());
                    existing.setStartTime(coachClass.getStartTime());
                    existing.setEndTime(coachClass.getEndTime());
                    existing.setType(coachClass.getType());
                    existing.setRoomName(coachClass.getRoomName());
                    existing.setNote(coachClass.getNote());
                    existing.setTargetArea(coachClass.getTargetArea());
                    existing.setEquipment(coachClass.getEquipment());
                    existing.setSessionNumber(coachClass.getSessionNumber());
                    existing.setTotalSessions(coachClass.getTotalSessions());
                    existing.setMemberNote(coachClass.getMemberNote());
                    return coachClassRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("课程记录不存在"));
    }

    @Override
    @Transactional
    public CoachClass updateCoachClassStatus(Integer id, CoachClassStatus status) {
        return coachClassRepository.findById(id)
                .map(coachClass -> {
                    coachClass.setStatus(status);
                    coachClass.setStatusText(getStatusText(status));
                    return coachClassRepository.save(coachClass);
                })
                .orElseThrow(() -> new RuntimeException("课程记录不存在"));
    }

    @Override
    @Transactional
    public void deleteCoachClass(Integer id) {
        coachClassRepository.deleteById(id);
    }
}
