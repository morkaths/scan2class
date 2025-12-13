package com.morkath.scan2class.entity.classroom;

import javax.persistence.*;

import com.morkath.scan2class.constant.classroom.ClassRole;
import com.morkath.scan2class.core.BaseAudit;
import com.morkath.scan2class.entity.auth.UserEntity;

@Entity
@Table(name = "class_participants")
public class ClassParticipantEntity extends BaseAudit {

	@EmbeddedId
	private ClassParticipantId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("classId")
	@JoinColumn(name = "class_id")
	private ClassroomEntity classroom;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 50)
	private ClassRole role = ClassRole.STUDENT;

	public ClassParticipantEntity() {
		super();
	}

	public ClassParticipantEntity(ClassroomEntity classroom, UserEntity user) {
		super();
		this.id = new ClassParticipantId(classroom.getId(), user.getId());
		this.classroom = classroom;
		this.user = user;
	}
	
	public ClassParticipantEntity(ClassroomEntity classroom, UserEntity user, ClassRole role) {
		super();
		this.id = new ClassParticipantId(classroom.getId(), user.getId());
		this.classroom = classroom;
		this.user = user;
		this.role = role;
	}

//	Setters and Getters

	public ClassParticipantId getId() {
		return id;
	}

	public ClassroomEntity getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassroomEntity classroom) {
		this.classroom = classroom;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public ClassRole getRole() {
		return role;
	}

	public void setRole(ClassRole role) {
		this.role = role;
	}

}
