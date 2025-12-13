package com.morkath.scan2class.core;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity extends BaseAudit {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
        this.id = id;
    }
}
