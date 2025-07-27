package com.simjaemin.springauthservice.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.simjaemin.springauthservice.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
	Optional<User> findUserByEmail(String email);

	@Query("select u from User u join fetch u.roles where u.id = :id")
	Optional<User> findUserWithRolesByEmail(@Param("id") Long id);
}
