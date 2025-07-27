package com.simjaemin.springauthservice.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.simjaemin.springauthservice.common.exception.CustomException;
import com.simjaemin.springauthservice.common.security.PasswordManager;
import com.simjaemin.springauthservice.domain.user.dto.UserRequestDto;
import com.simjaemin.springauthservice.domain.user.dto.UserResponseDto;
import com.simjaemin.springauthservice.domain.user.entity.Role;
import com.simjaemin.springauthservice.domain.user.entity.User;
import com.simjaemin.springauthservice.domain.user.entity.UserRole;
import com.simjaemin.springauthservice.domain.user.repository.UserRepository;
import com.simjaemin.springauthservice.domain.user.service.UserService;

@SpringBootTest
@Transactional
@Rollback
public class UserServiceTest {

	@Autowired
	private PasswordManager passwordManager;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	private User user;

	private Long userId;

	@BeforeEach
	public void setUser() {
		UserRole userRole = UserRole.builder()
			.role(Role.ROLE_USER)
			.build();

		List<UserRole> roleList = new ArrayList<>();
		roleList.add(userRole);

		user = User.builder()
			.email("testEmail@email.com")
			.password(passwordManager.encode("testPassword"))
			.nickname("사용자")
			.roles(roleList)
			.build();

		userRepository.save(user);
		userId = user.getId();
	}

	@Test
	@DisplayName("정상적인 회원가입")
	public void signup_Success() {
		// given
		UserRequestDto.Signup request = new UserRequestDto.Signup(
			"test2Email@email.com",
			"testPassword",
			"사용자 2"
		);

		// when
		UserResponseDto.Signup response = userService.signup(request);

		// then
		assertThat(response.getEmail()).isEqualTo(request.getEmail());
		assertThat(response.getNickname()).isEqualTo(request.getNickname());
		assertThat(response.getRoles().get(0)).isEqualTo(Role.ROLE_USER);
	}

	@Test
	@DisplayName("가입된 이메일을 사용한 회원가입의 경우")
	public void signup_Fail_DuplicateEmail() {
		// given
		UserRequestDto.Signup request = new UserRequestDto.Signup(
			"testEmail@email.com",
			"testPassword",
			"사용자 2"
		);

		// when
		CustomException exception = assertThrows(CustomException.class, () -> userService.signup(request));

		// then
		assertThat(exception.getMessage()).isEqualTo("이미 가입된 사용자입니다.");
	}

	@Test
	@DisplayName("정상적인 로그인")
	public void login_Success() {
		// given
		UserRequestDto.Login request = new UserRequestDto.Login(
			"testEmail@email.com",
			"testPassword"
		);

		// when
		UserResponseDto.Login response = userService.login(request);

		// then
		assertThat(response.getToken()).startsWith("Bearer ");
	}

	@Test
	@DisplayName("존재하지 않는 이메일을 사용한 로그인의 경우")
	public void login_Fail_WrongEmail() {
		// given
		UserRequestDto.Login request = new UserRequestDto.Login(
			"wrongEmail@email.com",
			"testPassword"
		);

		// when
		CustomException exception = assertThrows(CustomException.class, () -> userService.login(request));

		// then
		assertThat(exception.getMessage()).isEqualTo("아이디 또는 비밀번호가 올바르지 않습니다.");
	}

	@Test
	@DisplayName("잘못된 비밀번호를 사용한 로그인의 경우")
	public void login_Fail_WrongPassword() {
		// given
		UserRequestDto.Login request = new UserRequestDto.Login(
			"testEmail@email.com",
			"wrongPassword"
		);

		// when
		CustomException exception = assertThrows(CustomException.class, () -> userService.login(request));

		// then
		assertThat(exception.getMessage()).isEqualTo("아이디 또는 비밀번호가 올바르지 않습니다.");
	}

	@Test
	@DisplayName("정상적인 권한 부여")
	public void addRole_Success() {
		// when
		UserResponseDto.AddRole response = userService.addRole(userId);
		// CustomException exception = assertThrows(CustomException.class, () -> userService.login(request));

		// then
		assertThat(response.getEmail()).isEqualTo(user.getEmail());
		assertThat(response.getNickname()).isEqualTo(user.getNickname());
		assertThat(response.getRoles()).hasSize(2);
		assertThat(response.getRoles().get(0)).isEqualTo(Role.ROLE_USER);
		assertThat(response.getRoles().get(1)).isEqualTo(Role.ROLE_ADMIN);
	}

	@Test
	@DisplayName("존재하지 않는 유저에게 권한 부여한 경우")
	public void addRole_Fail_UserNotFound() {
		// given
		long otherUserId = userId + 100;

		// when
		CustomException exception = assertThrows(CustomException.class, () -> userService.addRole(otherUserId));

		// then
		assertThat(exception.getMessage()).isEqualTo("사용자 정보를 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("권한을 가지고 있는 유저에게 권한을 부여한 경우")
	public void addRole_Fail_UserAlreadyHasRole() {
		// given
		userService.addRole(userId);

		// when
		CustomException exception = assertThrows(CustomException.class, () -> userService.addRole(userId));

		// then
		assertThat(exception.getMessage()).isEqualTo("사용자가 이미 해당 권한을 가지고 있습니다.");
	}
}
