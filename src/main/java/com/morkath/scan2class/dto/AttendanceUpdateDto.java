package com.morkath.scan2class.dto;

import java.time.LocalDateTime;

public class AttendanceUpdateDto {
    private Long sessionId;
    private Long userId;
    private String username;
    private String fullname;
    private LocalDateTime checkinTime;
    private String status;
    private String deviceInfo;

    public AttendanceUpdateDto() {
    }

    public AttendanceUpdateDto(Long sessionId, Long userId, String username, String fullname, LocalDateTime checkinTime,
            String status, String deviceInfo) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
        this.checkinTime = checkinTime;
        this.status = status;
        this.deviceInfo = deviceInfo;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
