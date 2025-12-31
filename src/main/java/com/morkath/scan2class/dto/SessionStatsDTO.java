package com.morkath.scan2class.dto;

public class SessionStatsDTO {
    private Long sessionId;
    private String sessionName;
    private int totalStudents; // Total participants in class
    private int presentCount;
    private int lateCount;
    private int absentCount; // Calculated as totalStudents - (present + late)
    private int noRecordCount; // Explicit count of those with no record (usually same as absent if we treat
                               // no-record as absent)

    public SessionStatsDTO() {
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public int getNoRecordCount() {
        return noRecordCount;
    }

    public void setNoRecordCount(int noRecordCount) {
        this.noRecordCount = noRecordCount;
    }
}
