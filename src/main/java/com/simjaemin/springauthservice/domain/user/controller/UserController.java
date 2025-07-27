package com.simjaemin.springauthservice.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simjaemin.springauthservice.common.response.CommonResponse;
import com.simjaemin.springauthservice.common.response.enumcode.SuccessCode;
import com.simjaemin.springauthservice.domain.user.dto.UserRequestDto;
import com.simjaemin.springauthservice.domain.user.dto.UserResponseDto;
import com.simjaemin.springauthservice.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {

	private final UserService userService;

	// 회원가입
	@PostMapping("/signup")
	@Operation(
		summary = "회원가입",
		description = "새로운 사용자 계정을 생성합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "회원가입 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
							"timestamp": "2024-07-28 10:30:00",
							"message": "성공적으로 회원가입되었습니다.",
							"data": {
						         "email": "testEmail@email.com",
						         "nickname": "test 유저",
						         "roles": [
						         	"ROLE_USER"
						         ]
						     }
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 형식의 이메일 또는 비밀번호",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
							"timestamp": "2024-07-28 10:30:00",
							"message": "요청한 값에 오류가 있습니다.",
							"data": {
						         "email" : "올바른 이메일 형식이 아닙니다.",
						         "password": "비밀번호는 8자 이상 30자 이하여야 합니다."
						     }
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "409",
			description = "계정이 존재하는 이메일",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
							"timestamp": "2024-07-28 10:30:00",
							"message" : "이미 가입된 사용자입니다."
						}
						"""
				)
			)
		)
	})
	public ResponseEntity<CommonResponse<UserResponseDto.Signup>> signup(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "회원가입 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = com.simjaemin.springauthservice.domain.user.dto.UserRequestDto.Signup.class)
			)
		)
		@Valid @RequestBody UserRequestDto.Signup requestDto) {
		return CommonResponse.of(SuccessCode.USER_SIGNUP, userService.signup(requestDto));
	}

	// 로그인
	@PostMapping("/login")
	@Operation(
		summary = "로그인",
		description = "사용자가 로그인을 합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그인 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
							"timestamp": "2024-07-28 10:30:00",
							"message": "성공적으로 로그인되었습니다.",
							"data": {
						         "token": "Bearer token..."
						     }
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "로그인 요청 데이터 오류",
			content = @Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(
						summary = "이메일 또는 비밀번호 불일치",
						description = "입력된 이메일 또는 비밀번호가 일치하지 않는 경우",
						value = """
							{
								"timestamp": "2024-07-28 10:30:00",
								"message" : "이메일 또는 비밀번호가 올바르지 않습니다."
							}
							"""
					),
					@ExampleObject(
						summary = "이메일 형식 오류",
						description = "입력된 이메일의 형식이 잘못된 경우",
						value = """
							{
								"timestamp": "2024-07-28 10:30:00",
								"message": "요청한 값에 오류가 있습니다.",
								"data": {
							         "email" : "올바른 이메일 형식이 아닙니다."
							    }
							}
							"""
					)
				}
			)
		)
	})
	public ResponseEntity<CommonResponse<UserResponseDto.Login>> login(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "로그인 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = com.simjaemin.springauthservice.domain.user.dto.UserRequestDto.Login.class)
			)
		)
		@Valid @RequestBody UserRequestDto.Login requestDto) {
		return CommonResponse.of(SuccessCode.USER_LOGIN, userService.login(requestDto));
	}

	// 관리자 권한 부여
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/admin/users/{userId}/roles")
	@Operation(
		summary = "관리자 권한 부여",
		description = "사용자에게 관리자 권한을 부여, ADMIN 권한이 필요",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "권한 부여 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
							"timestamp": "2024-07-28 10:30:00",
							"message": "성공적으로 로그인되었습니다.",
							"data": {
						         "email": "test@email.com",
						         "nickname": "사용자 1",
						         "roles" : [
						         "ROLE_USER",
						         "ROLE_ADMIN"
						         ]
						    }
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "존재하지 않는 사용자의 ID",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
							"timestamp": "2024-07-28 10:30:00",
							"message": "사용자 정보를 찾을 수 없습니다."
						}
						"""
				)
			)
		)
	})
	public ResponseEntity<CommonResponse<UserResponseDto.AddRole>> addRole(
		@Parameter(
			description = "권한을 부여할 사용자의 ID",
			required = true,
			example = "1"
		)
		@PathVariable Long userId) {
		return CommonResponse.of(SuccessCode.ADMIN_ROLE_GRANTED, userService.addRole(userId));
	}
}
