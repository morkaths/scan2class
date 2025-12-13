package com.morkath.scan2class.constant.auth;

public enum PermissionCode {
	USER_VIEW("user:view"), USER_EDIT("user:edit"), ROLE_VIEW("role:view"), ROLE_EDIT("role:edit"),
	CLASS_VIEW("class:view"), CLASS_EDIT("class:edit");

	private final String code;

	PermissionCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
