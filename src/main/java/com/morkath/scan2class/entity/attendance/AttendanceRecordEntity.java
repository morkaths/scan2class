package com.morkath.scan2class.entity.attendance;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morkath.scan2class.core.BaseEntity;
import com.morkath.scan2class.entity.auth.UserEntity;

@Entity
@Table(name = "attendance_records", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "session_id", "user_id" }) })
public class AttendanceRecordEntity extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private SessionEntity session;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "checkin", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime checkin;

	@Column(name = "status", nullable = false, length = 50)
	private String status = "PRESENT";

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "distance")
	private Double distance;

	@Column(name = "device_uid", length = 100)
	private String deviceUid;

	@Column(name = "device_info", length = 500)
	private String deviceInfo;

	@Column(name = "cheat_status", nullable = false, length = 50)
	private String cheatStatus = "CLEAN";

	public AttendanceRecordEntity() {
		super();
	}

	public AttendanceRecordEntity(SessionEntity session, UserEntity user) {
		super();
		this.session = session;
		this.user = user;
		this.checkin = LocalDateTime.now();
		this.status = "PRESENT";
		this.cheatStatus = "CLEAN";
	}

	public AttendanceRecordEntity(SessionEntity session, UserEntity user, String status, Double latitude, Double longitude,
			Double distance, String deviceUid, String deviceInfo, String cheatStatus) {
		super();
		this.session = session;
		this.user = user;
		this.checkin = LocalDateTime.now();
		this.status = status;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
		this.deviceUid = deviceUid;
		this.deviceInfo = deviceInfo;
		this.cheatStatus = cheatStatus;
	}
	
//	Lifecycle Methods
	
	@PrePersist
	public void prePersist() {
		if (this.checkin == null) {
			this.checkin = LocalDateTime.now();
		}
	}
	
//	Business Methods
	
	public void calculateStatus() {
		if (this.checkin == null)
			this.checkin = LocalDateTime.now();
		LocalDateTime start = session.getStartTime();
		LocalDateTime end = session.getEndTime();
		LocalDateTime lateThreshold = start.plusMinutes(15);

		if (this.checkin.isBefore(start)) {
			this.status = "PRESENT";
		} else if (this.checkin.isAfter(end)) {
			this.status = "REJECTED";
		} else if (this.checkin.isAfter(lateThreshold)) {
			this.status = "LATE";
		} else {
			this.status = "PRESENT";
		}
	}
	
//	Getters and Setters

	public SessionEntity getSession() {
		return session;
	}

	public void setSession(SessionEntity session) {
		this.session = session;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public LocalDateTime getCheckin() {
		return checkin;
	}

	public void setCheckin(LocalDateTime checkin) {
		this.checkin = checkin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getDeviceUid() {
		return deviceUid;
	}

	public void setDeviceUid(String deviceUid) {
		this.deviceUid = deviceUid;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getCheatStatus() {
		return cheatStatus;
	}

	public void setCheatStatus(String cheatStatus) {
		this.cheatStatus = cheatStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AttendanceRecordEntity that = (AttendanceRecordEntity) o;
		return Objects.equals(session, that.session) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(session, user);
	}

}
