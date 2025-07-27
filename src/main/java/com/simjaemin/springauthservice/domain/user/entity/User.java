package com.simjaemin.springauthservice.domain.user.entity;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_user")
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	private String email;

	@Column(length = 300, nullable = false)
	private String password;

	@Column(length = 30, nullable = false)
	private String nickname;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<UserRole> roles;

	@Builder
	public User(String email, String password, String nickname, List<UserRole> roles) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.roles = roles;
	}

	public List<Role> getRolesToEnum() {
		return this.roles.stream()
			.map(UserRole::getRole)
			.collect(Collectors.toList());
	}

	public List<String> getRolesToString() {
		return this.roles.stream()
			.map(UserRole::getRole)
			.map(Enum::name)
			.collect(Collectors.toList());
	}

	public void addRole(Role role) {
		UserRole userRole = UserRole.builder()
			.user(this)
			.role(role)
			.build();
		this.roles.add(userRole);
	}

	public boolean hasRole(Role role) {
		for (UserRole userRole : roles) {
			if (userRole.hasRole(role)) {
				return true;
			}
		}
		return false;
	}
}
