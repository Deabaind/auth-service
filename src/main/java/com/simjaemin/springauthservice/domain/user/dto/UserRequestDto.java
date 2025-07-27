package com.simjaemin.springauthservice.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequestDto {

	@Getter
	@AllArgsConstructor
	@Schema(description = "회원가입 요청")
	public static class Signup {

		@Schema(description = "이메일", example = "test@email.com", required = true)
		@NotBlank(message = "필수 입력 항목입니다.")
		@Email(message = "올바른 이메일 형식이 아닙니다.")
		@Size(max = 50, message = "이메일은 50자 이하여야 합니다.")
		public String email;

		@Schema(description = "비밀번호", example = "testPassword", required = true)
		@NotBlank(message = "필수 입력 항목입니다.")
		@Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다.")
		public String password;

		@Schema(description = "닉네임", example = "사용자 1", required = true)
		@NotBlank(message = "필수 입력 항목입니다.")
		@Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하여야 합니다.")
		public String nickname;
	}

	@Getter
	@AllArgsConstructor
	public static class Login {

		@NotBlank(message = "필수 입력 항목입니다.")
		@Email(message = "올바른 이메일 형식이 아닙니다.")
		@Size(max = 50, message = "이메일은 50자 이하여야 합니다.")
		public String email;

		@NotBlank(message = "필수 입력 항목입니다.")
		@Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다.")
		public String password;
	}
}
