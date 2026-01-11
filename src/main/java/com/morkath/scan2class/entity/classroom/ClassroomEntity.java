package com.morkath.scan2class.entity.classroom;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.morkath.scan2class.core.BaseEntity;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.entity.auth.UserEntity;

@Entity
@Table(name = "classes")
public class ClassroomEntity extends BaseEntity {

	@Column(name = "code", nullable = false, unique = true, length = 50)
	private String code;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "room", length = 50)
	private String room;

	@Column(name = "status", nullable = false)
	private int status = 1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private UserEntity owner;

	@OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<ClassParticipantEntity> participants = new HashSet<>();

	@OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("startTime DESC")
	private Set<SessionEntity> sessions = new HashSet<>();

	public ClassroomEntity() {
		super();
	}

	public ClassroomEntity(String name, String room, int status, UserEntity owner) {
		super();
		this.name = name;
		this.room = room;
		this.status = status;
		this.owner = owner;
	}

	// Helper Methods

	public void addParticipant(UserEntity user) {
		ClassParticipantEntity participant = new ClassParticipantEntity(this, user);
		this.participants.add(participant);
	}

	public void removeParticipant(UserEntity user) {
		participants
				.removeIf(participant -> participant.getUser().equals(user) && participant.getClassroom().equals(this));
	}

	public void addSession(SessionEntity session) {
		this.sessions.add(session);
		session.setClassroom(this);
	}

	public void removeSession(SessionEntity session) {
		this.sessions.remove(session);
		session.setClassroom(null);
	}

	// Getters and Setters

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	public Set<ClassParticipantEntity> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<ClassParticipantEntity> participants) {
		this.participants = participants;
	}

	public Set<SessionEntity> getSessions() {
		return sessions;
	}

	public void setSessions(Set<SessionEntity> sessions) {
		this.sessions = sessions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ClassroomEntity classroom = (ClassroomEntity) o;
		return getId() != null && getId().equals(classroom.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
