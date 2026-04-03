package org.example.fitnessjava.pojo.vo;

import lombok.Data;
import org.example.fitnessjava.pojo.BookingSource;
import org.example.fitnessjava.pojo.BookingStatus;

@Data
public class BookingVO {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer coachId;
    private String coachName;
    private String coachAvatar;
    private String specialty;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String location;
    private BookingStatus status;
    private String statusText;
    private BookingSource source;
    private String packageOrderId;
    private String phone;
}
