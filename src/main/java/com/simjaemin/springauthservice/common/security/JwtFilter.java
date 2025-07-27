package com.simjaemin.springauthservice.common.security;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.simjaemin.springauthservice.common.exception.CustomException;
import com.simjaemin.springauthservice.common.errorcode.ErrorCode;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final WhiteList whiteList;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		HttpMethod method = HttpMethod.valueOf(request.getMethod());
		String uri = request.getRequestURI();

		if (whiteList.isWhiteList(method, uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = jwtProvider.resolveAccessToken(request);

		if (StringUtils.hasText(accessToken)) {
			jwtProvider.validateToken(accessToken);
			setAuthentication(accessToken, request);
			filterChain.doFilter(request, response);
			return;
		}
		throw new CustomException(ErrorCode.INVALID_TOKEN);
	}

	private void setAuthentication(String accessToken, HttpServletRequest request) {
		Claims claims = jwtProvider.getClaims(accessToken);
		CustomPrincipal principal = jwtProvider.getPrincipal(claims);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal,
			null, principal.getAuthorities());

		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}
}
