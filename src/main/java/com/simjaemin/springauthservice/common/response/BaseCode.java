package com.simjaemin.springauthservice.common.response;

import org.springframework.http.HttpStatus;

public interface BaseCode {
	HttpStatus getHttpStatus();

	String getMessage();
}
