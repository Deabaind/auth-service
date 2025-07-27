package com.simjaemin.springauthservice.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simjaemin.springauthservice.common.response.enumcode.ErrorCode;
import com.simjaemin.springauthservice.common.exception.CustomException;
import com.simjaemin.springauthservice.common.security.JwtProvider;
import com.simjaemin.springauthservice.common.security.PasswordManager;
import com.simjaemin.springauthservice.domain.user.dto.UserRequestDto;
import com.simjaemin.springauthservice.domain.user.dto.UserResponseDto;
import com.simjaemin.springauthservice.domain.user.entity.Role;
import com.simjaemin.springauthservice.domain.user.entity.User;
import com.simjaemin.springauthservice.domain.user.entity.UserRole;
import com.simjaemin.springauthservice.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordManager passwordManager;
	private final JwtProvider jwtProvider;

	// id 기준 유저 조회
	public User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	// id 기준 유저와 권한 조회
	public User findUserWithRolesById(Long userId) {
		return userRepository.findUserWithRolesByEmail(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	// email 기준 유저 조회
	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.DUPLICATE_EMAIL));
	}

	// 회원 가입
	@Transactional
	public UserResponseDto.Signup signup(UserRequestDto.Signup requestDto) {
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
		}

		String encodePassword = passwordManager.encode(requestDto.getPassword());

		UserRole role = UserRole.builder()
			.role(Role.ROLE_USER)
			.build();

		User saveUser = userRepository.save(User.builder()
			.email(requestDto.getEmail())
			.password(encodePassword)
			.nickname(requestDto.getNickname())
			.roles(List.of(role))
			.build());

		return UserResponseDto.Signup.from(saveUser);
	}

	// 로그인
	@Transactional
	public UserResponseDto.Login login(UserRequestDto.Login requestDto) {

		User user = findUserByEmail(requestDto.getEmail());

		passwordManager.matches(requestDto.getPassword(), user.getPassword());
		String token = jwtProvider.createAccessToken(
			user.getId(),
			user.getEmail(),
			user.getNickname(),
			user.getRolesToString()
		);

		return UserResponseDto.Login.from(token);
	}

	// 권한 요청
	@Transactional
	public UserResponseDto.AddRole addRole(Long userId) {
		User user = findUserWithRolesById(userId);

		if (user.hasRole(Role.ROLE_ADMIN)) {
			throw new CustomException(ErrorCode.USER_ALREADY_HAS_ROLE);
		}
		user.addRole(Role.ROLE_ADMIN);
		return UserResponseDto.AddRole.from(user);
	}
}
