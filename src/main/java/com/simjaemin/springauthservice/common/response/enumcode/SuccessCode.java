package com.simjaemin.springauthservice.common.response.enumcode;

import org.springframework.http.HttpStatus;

import com.simjaemin.springauthservice.common.response.BaseCode;

import lombok.Getter;

@Getter
public enum SuccessCode implements BaseCode {
	USER_SIGNUP(HttpStatus.CREATED, "성공적으로 회원가입되었습니다."),
	USER_LOGIN(HttpStatus.OK, "성공적으로 로그인되었습니다."),
	ADMIN_ROLE_GRANTED(HttpStatus.OK, "성공적으로 권한이 부여되었습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	SuccessCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
