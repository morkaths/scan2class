package com.morkath.scan2class.dto;

import java.util.Map;
import java.util.List;

public class ClassroomStatsDTO {
    private Long classroomId;
    private String classroomName;
    private int totalSessions;
    private int totalStudents;

    // Aggregated stats for the whole class
    private Map<String, Integer> overallDidAttend; // e.g., "PRESENT": 50, "ABSENT": 10

    // Detailed list per student
    private List<StudentStatDTO> studentStats;

    public ClassroomStatsDTO() {
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Map<String, Integer> getOverallDidAttend() {
        return overallDidAttend;
    }

    public void setOverallDidAttend(Map<String, Integer> overallDidAttend) {
        this.overallDidAttend = overallDidAttend;
    }

    public List<StudentStatDTO> getStudentStats() {
        return studentStats;
    }

    public void setStudentStats(List<StudentStatDTO> studentStats) {
        this.studentStats = studentStats;
    }
}
