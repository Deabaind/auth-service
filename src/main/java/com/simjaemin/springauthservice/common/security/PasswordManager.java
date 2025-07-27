package com.simjaemin.springauthservice.common.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.simjaemin.springauthservice.common.exception.CustomException;
import com.simjaemin.springauthservice.common.response.enumcode.ErrorCode;

@Component
public class PasswordManager {

	private PasswordEncoder passwordEncoder;

	public PasswordManager() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	// password 암호화
	public String encode(String password) {
		return passwordEncoder.encode(password);
	}

	// password 검증
	public void matches(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
		}
	}
}
