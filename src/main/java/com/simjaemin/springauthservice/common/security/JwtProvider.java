package com.simjaemin.springauthservice.common.security;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.simjaemin.springauthservice.common.exception.CustomException;
import com.simjaemin.springauthservice.common.errorcode.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtProvider {

	@Value("${jwt.secret}")
	private String secretString;

	private Key secretKey;

	@Value("${jwt.access-token-expiration}")
	private long tokenExpiryDate;

	@PostConstruct
	public void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secretString);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	private final String TOKEN_PREFIX = "Bearer ";
	private final String CLAIM_EMAIL = "email";
	private final String CLAIM_NICKNAME = "nickname";
	public static final String CLAIM_ROLES = "roles";

	// accessToken 발급
	public String createAccessToken(Long userId, String email, String nickname, List<String> roles) {
		Date now = new Date();

		return TOKEN_PREFIX + Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim(CLAIM_EMAIL, email)
			.claim(CLAIM_NICKNAME, nickname)
			.claim(CLAIM_ROLES, roles)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenExpiryDate))
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// access token 조회
	public String resolveAccessToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (!StringUtils.hasText(token)) {
			return null;
		}
		if (token.startsWith(TOKEN_PREFIX)) {
			return token.substring(TOKEN_PREFIX.length());
		}
		return token;
	}

	public Claims getClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		} catch (SignatureException e) {
			throw new CustomException(ErrorCode.WRONG_SIGNATURE);
		} catch (JwtException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	public CustomPrincipal getPrincipal(Claims claims) {
		List<String> roleList = claims.get(CLAIM_ROLES, List.class);
		List<SimpleGrantedAuthority> authorities = roleList.stream()
			.map(SimpleGrantedAuthority::new)
			.toList();

		return new CustomPrincipal(
			Long.valueOf(claims.getSubject()),
			claims.get(CLAIM_EMAIL, String.class),
			claims.get(CLAIM_NICKNAME, String.class),
			authorities
		);
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		} catch (SignatureException e) {
			throw new CustomException(ErrorCode.WRONG_SIGNATURE);
		} catch (JwtException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}
}
