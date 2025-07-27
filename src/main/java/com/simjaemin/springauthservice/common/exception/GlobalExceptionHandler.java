package com.simjaemin.springauthservice.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.simjaemin.springauthservice.common.response.CommonResponse;
import com.simjaemin.springauthservice.common.response.enumcode.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse<Map<String, String>>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		Map<String, String> errorResponse = new HashMap<>();
		e.getBindingResult().getFieldErrors()
			.forEach(error -> {
				errorResponse.put(error.getField(), error.getDefaultMessage());
			});
		return CommonResponse.of(ErrorCode.INVALID_INPUT, errorResponse);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CommonResponse<Void>> customExceptionHandler(CustomException e) {
		return CommonResponse.of(e.getErrorCode(), null);
	}
}
