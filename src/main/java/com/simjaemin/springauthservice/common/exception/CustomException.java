package com.simjaemin.springauthservice.common.exception;

import com.simjaemin.springauthservice.common.errorcode.ErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
