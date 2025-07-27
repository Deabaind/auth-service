package com.simjaemin.springauthservice.common.response.enumcode;

import org.springframework.http.HttpStatus;

import com.simjaemin.springauthservice.common.response.BaseCode;

import lombok.Getter;

@Getter
public enum ErrorCode implements BaseCode {

	// common
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "요청한 값에 오류가 있습니다."),

	// auth
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
	WRONG_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),

	// user
	USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 사용자입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "아이디 또는 비밀번호가 올바르지 않습니다."),
	INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
	USER_ALREADY_HAS_ROLE(HttpStatus.CONFLICT, "사용자가 이미 해당 권한을 가지고 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
