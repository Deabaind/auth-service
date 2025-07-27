package com.simjaemin.springauthservice.common.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class WhiteList {

	private final PathMatcher pathMatcher;

	private List<String> postUri = new ArrayList<>();
	private List<String> getUri = new ArrayList<>();

	private Map<HttpMethod, List<String>> whiteListMap;

	@PostConstruct
	public void init() {
		// postUri.add("/signup");
		// postUri.add("/login");
		// postUri.add("/h2-console/**");
		// postUri.add("/swagger-ui/**");
		// postUri.add("/v3/api-docs");
		// postUri.add("/v3/api-docs/swagger-config");
		//
		// getUri.add("/h2-console/**");
		// getUri.add("/swagger-ui/**");
		// getUri.add("/v3/api-docs");
		// getUri.add("/v3/api-docs/swagger-config");

		postUri.add("/signup");
		postUri.add("/login");
		postUri.add("/h2-console/**");
		postUri.add("/swagger-ui/**");
		postUri.add("/v3/api-docs");
		postUri.add("/v3/api-docs/**");           // 추가
		postUri.add("/v3/api-docs/swagger-config");
		postUri.add("/swagger-resources/**");      // 추가
		postUri.add("/webjars/**");

		getUri.add("/h2-console/**");
		getUri.add("/swagger-ui/**");
		getUri.add("/swagger-ui.html");           // 추가
		getUri.add("/swagger-ui/index.html");     // 추가
		getUri.add("/v3/api-docs");
		getUri.add("/v3/api-docs/**");            // 추가
		getUri.add("/v3/api-docs/swagger-config");
		getUri.add("/swagger-resources/**");      // 추가
		getUri.add("/webjars/**");

		whiteListMap = Map.of(
			HttpMethod.POST, postUri,
			HttpMethod.GET, getUri
		);
	}

	public boolean isWhiteList(HttpMethod method, String uri) {
		if (method == null || uri == null) {
			return false;
		}
		List<String> patterns = whiteListMap.get(method);
		if (patterns == null) {
			return false;
		}
		for (String pattern : patterns) {
			if (pathMatcher.match(pattern, uri)) {
				return true;
			}
		}
		return false;
	}

	public String[] getMethodPattern(HttpMethod method) {
		List<String> arrayPattern = whiteListMap.get(method);
		return arrayPattern.toArray(new String[arrayPattern.size()]);
	}
}
