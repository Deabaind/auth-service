package com.simjaemin.springauthservice.domain.user.dto;

import java.util.List;

import com.simjaemin.springauthservice.domain.user.entity.Role;
import com.simjaemin.springauthservice.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class UserResponseDto {

	@Getter
	public static class Signup {

		private final String email;

		private final String nickname;

		private final List<Role> roles;

		private Signup(String email, String nickname, List<Role> roles) {
			this.email = email;
			this.nickname = nickname;
			this.roles = roles;
		}

		public static Signup from(User user) {
			return new Signup(
				user.getEmail(),
				user.getNickname(),
				user.getRolesToEnum()
			);
		}
	}

	@Getter
	public static class Login {

		private final String token;

		private Login(String token) {
			this.token = token;
		}

		public static Login from(String token) {
			return new Login(token);
		}
	}

	@Getter
	public static class AddRole {

		private final String email;

		private final String nickname;

		private final List<Role> roles;

		private AddRole(String email, String nickname, List<Role> roles) {
			this.email = email;
			this.nickname = nickname;
			this.roles = roles;
		}

		public static AddRole from(User user) {
			return new AddRole(
				user.getEmail(),
				user.getNickname(),
				user.getRolesToEnum()
			);
		}
	}
}
