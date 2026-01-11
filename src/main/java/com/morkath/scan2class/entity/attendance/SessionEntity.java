package com.morkath.scan2class.entity.attendance;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morkath.scan2class.core.BaseEntity;
import com.morkath.scan2class.entity.classroom.ClassroomEntity;
import com.morkath.scan2class.util.GeoUtils;

@Entity
@Table(name = "sessions")
public class SessionEntity extends BaseEntity {

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "room", length = 50)
	private String room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private ClassroomEntity classroom;

	@Column(name = "start_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;

	@Column(name = "end_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	@Column(name = "active")
	private boolean active = false;

	@Column(name = "token", unique = true, length = 500)
	private String token;

	@Column(name = "token_expiry")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime tokenExpiry;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "radius")
	private Double radius = 50.0;

	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AttendanceRecordEntity> records = new HashSet<>();

	public SessionEntity() {
		super();
		this.startTime = LocalDateTime.now();
		this.token = UUID.randomUUID().toString();
	}

	public SessionEntity(String name, ClassroomEntity classroom) {
		super();
		this.name = name;
		this.classroom = classroom;
		this.startTime = LocalDateTime.now();
		this.token = UUID.randomUUID().toString();
		this.active = false;
	}

	public SessionEntity(String name, ClassroomEntity classroom, LocalDateTime startTime, LocalDateTime endTime,
			boolean active, String token, LocalDateTime tokenExpiry, Double latitude, Double longitude, Double radius) {
		super();
		this.name = name;
		this.classroom = classroom;
		this.startTime = startTime;
		this.endTime = endTime;
		this.active = active;
		this.token = token;
		this.tokenExpiry = tokenExpiry;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}

	// Business Methods

	public boolean isTokenValid() {
		if (this.tokenExpiry == null) {
			return false;
		}
		return LocalDateTime.now().isBefore(this.tokenExpiry);
	}

	public double calculateDistance(Double userLat, Double userLong) {
		return GeoUtils.calculateDistance(this.latitude, this.longitude, userLat, userLong);
	}

	public boolean isWithinRadius(Double userLat, Double userLong) {
		double distance = calculateDistance(userLat, userLong);
		return distance <= this.radius;
	}

	/**
	 * Lấy bản đồ ánh xạ giữa User ID và AttendanceRecordEntity.
	 * 
	 * @return Map<User ID, AttendanceRecordEntity>
	 */
	@Transient
	public Map<Long, AttendanceRecordEntity> getAttendanceMap() {
		return records.stream().collect(Collectors.toMap(record -> record.getUser().getId(), record -> record,
				(existing, replacement) -> existing));
	}

	// Helper Methods

	public void addRecord(AttendanceRecordEntity record) {
		this.records.add(record);
		record.setSession(this);
	}

	public void removeRecord(AttendanceRecordEntity record) {
		this.records.remove(record);
		record.setSession(null);
	}

	/**
	 * Trả về TRUE nếu phiên đang mở (thời gian hiện tại chưa vượt quá EndTime).
	 * 
	 * @Transient: Báo cho Hibernate biết đây không phải cột trong DB.
	 */
	@Transient
	public boolean getIsOpen() {
		if (!this.active)
			return false;
		if (this.endTime == null)
			return true;
		return LocalDateTime.now().isBefore(this.endTime);
	}

	// Getters and Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public ClassroomEntity getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassroomEntity classroom) {
		this.classroom = classroom;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getTokenExpiry() {
		return tokenExpiry;
	}

	public void setTokenExpiry(LocalDateTime tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Set<AttendanceRecordEntity> getRecords() {
		return records;
	}

	public void setRecords(Set<AttendanceRecordEntity> records) {
		this.records = records;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SessionEntity session = (SessionEntity) o;
		return getId() != null && getId().equals(session.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
