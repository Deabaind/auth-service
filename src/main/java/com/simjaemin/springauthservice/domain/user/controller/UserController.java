package com.simjaemin.springauthservice.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simjaemin.springauthservice.domain.user.dto.UserRequestDto;
import com.simjaemin.springauthservice.domain.user.dto.UserResponseDto;
import com.simjaemin.springauthservice.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
				schema = @Schema(implementation = UserResponseDto.Signup.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 데이터",
			content = @Content(
				schema = @Schema(implementation = UserResponseDto.Signup.class)
			)
		),
		@ApiResponse(
			responseCode = "409",
			description = "이미 존재하는 이메일",
			content = @Content(
				schema = @Schema(implementation = UserResponseDto.Signup.class)
			)
		)
	})
	public ResponseEntity<UserResponseDto.Signup> signup(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "회원가입 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = UserResponseDto.Signup.class)
			)
		)
		@Valid @RequestBody UserRequestDto.Signup requestDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(requestDto));
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<UserResponseDto.Login> login(
		@Valid @RequestBody UserRequestDto.Login requestDto) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.login(requestDto));
	}

	// 관리자 권한 부여
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/users/{userId}/roles")
	@Operation(
		summary = "관리자 권한 부여",
		description = "사용자에게 관리자 권한을 부여합니다. ADMIN 권한이 필요합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "권한 부여 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = UserResponseDto.AddRole.class)
			)
		)
	})
	public ResponseEntity<UserResponseDto.AddRole> addRole(
		@Parameter(
			description = "권한을 부여할 사용자의 ID",
			required = true,
			example = "1"
		)
		@PathVariable Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.addRole(userId));
	}
}
