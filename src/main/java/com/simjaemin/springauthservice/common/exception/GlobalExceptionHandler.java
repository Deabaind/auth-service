package com.simjaemin.springauthservice.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		Map<String, String> errorResponse = new HashMap<>();
		e.getBindingResult().getFieldErrors()
			.forEach(error -> {
				errorResponse.put(error.getField(), error.getDefaultMessage());
			});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Map<String, String>> customExceptionHandler(CustomException e) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("message", e.getMessage());
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
	}
}
