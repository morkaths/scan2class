package com.morkath.scan2class.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateDto {
    private Long sessionId;
    private Long userId;
    private String username;
    private String fullname;
    private LocalDateTime checkinTime;
    private String status;
    private String deviceInfo;
}
