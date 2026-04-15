package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.SystemConfigRepository;
import org.example.fitnessjava.pojo.SystemConfig;
import org.example.fitnessjava.pojo.vo.CoachCompensationSettingsVO;
import org.example.fitnessjava.service.CoachCompensationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class CoachCompensationServiceImpl implements CoachCompensationService {

    private static final String KEY_LESSON_UNIT_PRICE = "coach.compensation.lessonUnitPrice";
    private static final String KEY_COACH_RATIO = "coach.compensation.coachRatio";
    private static final double DEFAULT_LESSON_UNIT_PRICE = 100.0;
    private static final double DEFAULT_COACH_RATIO = 0.5;

    @Resource
    private SystemConfigRepository systemConfigRepository;

    @Override
    @Transactional(readOnly = true)
    public CoachCompensationSettingsVO getSettings() {
        double lessonUnitPrice = getDoubleConfig(KEY_LESSON_UNIT_PRICE, DEFAULT_LESSON_UNIT_PRICE);
        double coachRatio = getDoubleConfig(KEY_COACH_RATIO, DEFAULT_COACH_RATIO);
        return buildVO(lessonUnitPrice, coachRatio);
    }

    @Override
    @Transactional
    public CoachCompensationSettingsVO updateSettings(Double lessonUnitPrice, Double coachRatio, String updatedBy) {
        if (lessonUnitPrice == null || lessonUnitPrice < 0) {
            throw new IllegalArgumentException("课时单价必须大于等于 0");
        }
        if (coachRatio == null || coachRatio < 0 || coachRatio > 1) {
            throw new IllegalArgumentException("教练比例必须在 0 到 1 之间");
        }

        saveConfig(KEY_LESSON_UNIT_PRICE, lessonUnitPrice, updatedBy, "教练授课结算课时单价");
        saveConfig(KEY_COACH_RATIO, coachRatio, updatedBy, "教练授课分成比例");

        return buildVO(lessonUnitPrice, coachRatio);
    }

    @Override
    @Transactional(readOnly = true)
    public Double estimateCoachFeePerClass() {
        CoachCompensationSettingsVO settings = getSettings();
        return settings.getCoachFeePerClass();
    }

    private CoachCompensationSettingsVO buildVO(Double lessonUnitPrice, Double coachRatio) {
        CoachCompensationSettingsVO vo = new CoachCompensationSettingsVO();
        double coachFee = round(lessonUnitPrice * coachRatio);
        vo.setLessonUnitPrice(round(lessonUnitPrice));
        vo.setCoachRatio(round(coachRatio));
        vo.setCoachFeePerClass(coachFee);
        vo.setBossFeePerClass(round(lessonUnitPrice - coachFee));

        Optional<SystemConfig> priceConfig = systemConfigRepository.findByConfigKey(KEY_LESSON_UNIT_PRICE);
        priceConfig.ifPresent(config -> {
            vo.setUpdatedBy(config.getUpdatedBy());
            vo.setUpdatedAt(config.getUpdateTime());
        });
        return vo;
    }

    private double getDoubleConfig(String key, double defaultValue) {
        return systemConfigRepository.findByConfigKey(key)
                .map(SystemConfig::getConfigValue)
                .map(value -> {
                    try {
                        return Double.parseDouble(value);
                    } catch (NumberFormatException ex) {
                        return defaultValue;
                    }
                })
                .orElse(defaultValue);
    }

    private void saveConfig(String key, Double value, String updatedBy, String description) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElseGet(SystemConfig::new);
        config.setConfigKey(key);
        config.setConfigValue(String.valueOf(value));
        config.setUpdatedBy(updatedBy);
        config.setDescription(description);
        systemConfigRepository.save(config);
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
