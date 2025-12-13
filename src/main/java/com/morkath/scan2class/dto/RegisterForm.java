package com.morkath.scan2class.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterForm {
	@NotBlank(message = "Tên đăng nhập không được để trống")
	@Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3–50 ký tự")
	private String username;
	
	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	private String email;
	
	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, max = 100, message = "Mật khẩu phải có ít nhất 6 ký tự")
	private String password;
	
	@NotBlank(message = "Xác nhận mật khẩu không được để trống")
	@Size(min = 6, max = 100, message = "Xác nhận mật khẩu phải có ít nhất 6 ký tự")
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
