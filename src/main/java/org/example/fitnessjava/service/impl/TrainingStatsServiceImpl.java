package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.BodyAssessmentRecordRepository;
import org.example.fitnessjava.dao.CourseOrderRepository;
import org.example.fitnessjava.pojo.BodyAssessmentRecord;
import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.example.fitnessjava.pojo.dto.TrainingMetricItem;
import org.example.fitnessjava.pojo.vo.TrainingStatsResponse;
import org.example.fitnessjava.service.TrainingStatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingStatsServiceImpl implements TrainingStatsService {

    @Resource
    private BodyAssessmentRecordRepository bodyAssessmentRecordRepository;

    @Resource
    private CourseOrderRepository courseOrderRepository;

    @Override
    public TrainingStatsResponse getTrainingStats(Integer userId) {
        List<BodyAssessmentRecord> records = bodyAssessmentRecordRepository.findByUserId(userId);
        records.sort(Comparator.comparing(BodyAssessmentRecord::getDate));

        Double currentWeight = null;
        List<TrainingMetricItem> metrics = new ArrayList<>();

        for (BodyAssessmentRecord record : records) {
            if (record.getWeight() != null) {
                currentWeight = record.getWeight();
            }
            if (record.getDate() != null && (record.getWeight() != null || record.getMuscleMass() != null)) {
                TrainingMetricItem item = new TrainingMetricItem();
                item.setDate(formatDate(record.getDate()));
                item.setWeight(record.getWeight());
                item.setMuscle(record.getMuscleMass());
                metrics.add(item);
            }
        }

        Integer monthTrainingCount = getMonthTrainingCount(userId);

        return new TrainingStatsResponse(monthTrainingCount, currentWeight, metrics);
    }

    @Override
    public List<BodyAssessmentRecord> getRecordsByUserId(Integer userId) {
        List<BodyAssessmentRecord> records = bodyAssessmentRecordRepository.findByUserId(userId);
        records.sort(Comparator.comparing(BodyAssessmentRecord::getDate).reversed());
        return records;
    }

    @Override
    public Optional<BodyAssessmentRecord> getRecordById(Integer userId, Integer id) {
        return bodyAssessmentRecordRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public BodyAssessmentRecord createRecord(Integer userId, BodyAssessmentRecord record) {
        record.setUserId(userId);
        if (record.getDate() == null) {
            record.setDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return bodyAssessmentRecordRepository.save(record);
    }

    @Override
    public Optional<BodyAssessmentRecord> updateRecord(Integer userId, Integer id, BodyAssessmentRecord record) {
        Optional<BodyAssessmentRecord> existing = bodyAssessmentRecordRepository.findByIdAndUserId(id, userId);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        BodyAssessmentRecord target = existing.get();
        if (record.getWeight() != null) {
            target.setWeight(record.getWeight());
        }
        if (record.getMuscleMass() != null) {
            target.setMuscleMass(record.getMuscleMass());
        }
        if (record.getBodyFat() != null) {
            target.setBodyFat(record.getBodyFat());
        }
        if (record.getBmi() != null) {
            target.setBmi(record.getBmi());
        }
        if (record.getVisceralFat() != null) {
            target.setVisceralFat(record.getVisceralFat());
        }
        if (record.getHeight() != null) {
            target.setHeight(record.getHeight());
        }
        if (record.getChest() != null) {
            target.setChest(record.getChest());
        }
        if (record.getWaist() != null) {
            target.setWaist(record.getWaist());
        }
        if (record.getHips() != null) {
            target.setHips(record.getHips());
        }
        if (record.getLeftArm() != null) {
            target.setLeftArm(record.getLeftArm());
        }
        if (record.getRightArm() != null) {
            target.setRightArm(record.getRightArm());
        }
        if (record.getLeftThigh() != null) {
            target.setLeftThigh(record.getLeftThigh());
        }
        if (record.getRightThigh() != null) {
            target.setRightThigh(record.getRightThigh());
        }
        if (record.getLeftCalf() != null) {
            target.setLeftCalf(record.getLeftCalf());
        }
        if (record.getRightCalf() != null) {
            target.setRightCalf(record.getRightCalf());
        }
        if (record.getCoachId() != null) {
            target.setCoachId(record.getCoachId());
        }
        if (record.getPhotos() != null) {
            target.setPhotos(record.getPhotos());
        }
        if (record.getNotes() != null) {
            target.setNotes(record.getNotes());
        }
        if (record.getDate() != null) {
            target.setDate(record.getDate());
        }
        return Optional.of(bodyAssessmentRecordRepository.save(target));
    }

    @Override
    public void deleteRecord(Integer userId, Integer id) {
        bodyAssessmentRecordRepository.findByIdAndUserId(id, userId)
                .ifPresent(bodyAssessmentRecordRepository::delete);
    }

    private Integer getMonthTrainingCount(Integer userId) {
        LocalDate now = LocalDate.now();
        String currentMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());

        List<CourseOrder> orders = courseOrderRepository.findByUserId(userId);
        int count = 0;
        for (CourseOrder order : orders) {
            if (order.getStatus() == CourseOrderStatus.ACTIVE || order.getStatus() == CourseOrderStatus.COMPLETED) {
                if (order.getUsedSessions() != null) {
                    count += order.getUsedSessions();
                }
            }
        }
        return count;
    }

    private String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.getMonthValue() + "/" + date.getDayOfMonth();
        } catch (DateTimeParseException e) {
            return dateStr;
        }
    }
}
