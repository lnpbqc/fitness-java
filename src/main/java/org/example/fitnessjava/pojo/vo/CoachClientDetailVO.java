package org.example.fitnessjava.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class CoachClientDetailVO {
    private Integer id;
    private String nickname;
    private String avatar;
    private String phone;
    private String gender;
    private Integer age;
    private List<String> tags;
    private String memberLevel;
    private Integer points;
    private String membershipExpireAt;
    private String joinDate;
    private ActiveCourseSummary activeCourse;
    private CoachBindingInfo coachBinding;
    private TrainingSummary trainingSummary;
    private AssessmentSummary assessmentSummary;

    @Data
    public static class ActiveCourseSummary {
        private String packageName;
        private Integer remainingSessions;
        private Integer totalSessions;
        private String endDate;
    }

    @Data
    public static class CoachBindingInfo {
        private Integer coachId;
        private String coachName;
        private String coachAvatar;
        private String specialty;
    }

    @Data
    public static class TrainingSummary {
        private Integer totalTrainingCount;
        private String lastTrainingDate;
    }

    @Data
    public static class AssessmentSummary {
        private Integer totalAssessmentCount;
        private String lastAssessmentDate;
        private Double latestWeight;
        private Double latestBodyFat;
    }
}
