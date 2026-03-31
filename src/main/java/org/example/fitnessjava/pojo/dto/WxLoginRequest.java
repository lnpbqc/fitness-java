package org.example.fitnessjava.pojo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WxLoginRequest {
    private String code;
    private String nickname;
    private String avatar;
}