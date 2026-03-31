package org.example.fitnessjava.pojo.vo;

import lombok.Data;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.example.fitnessjava.pojo.PackageType;

@Data
public class CourseOrderVO {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer packageId;
    private String packageName;
    private PackageType type;
    private Integer totalSessions;
    private Integer usedSessions;
    private Integer remainingSessions;
    private Integer validDays;
    private String startDate;
    private String endDate;
    private String purchaseDate;
    private Double price;
    private Integer pointsReward;
    private CourseOrderStatus status;
}
