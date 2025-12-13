package com.morkath.scan2class.entity.classroom;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@Embeddable
public class ClassParticipantId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "class_id", nullable = false)
	private Long classId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	public ClassParticipantId() {
		super();
	}

	public ClassParticipantId(Long classId, Long userId) {
		super();
		this.classId = classId;
		this.userId = userId;
	}

//	Setters and Getters

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ClassParticipantId that = (ClassParticipantId) o;
		return Objects.equals(classId, that.classId) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(classId, userId);
	}
}
