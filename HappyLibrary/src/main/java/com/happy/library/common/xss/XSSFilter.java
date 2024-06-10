package com.happy.library.common.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * XSS (Cross-Site Scripting) 공격을 방지하기 위해 사용되는 필터 클래스. RequestWrapper 클래스를 사용하여 요청을 래핑하여 처리합니다.
 */
public class XSSFilter implements Filter {
	// 필터 초기화 메서드
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 특별히 초기화할 작업이 없으므로 비워둡니다.
	}

	// 필터 종료 메서드
	@Override
	public void destroy() {
		// 특별히 종료할 작업이 없으므로 비워둡니다.
	}

	/**
	 * 필터 체인에 요청을 전달하기 전에 XSS 공격을 방지하기 위해 요청을 래핑합니다.
	 *
	 * @param request 서블릿 요청 객체
	 * @param response 서블릿 응답 객체
	 * @param filterChain 필터 체인 객체
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		// HttpServletRequest 객체를 RequestWrapper로 래핑하여 XSS 공격 방지 로직 적용
		filterChain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
	}

}
