package com.happy.library.common.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.StringEscapeUtils;

/**
 * HttpServletRequest를 래핑하여 XSS 공격에 취약한 문자열을 제거하거나 변조합니다.
 */
public class RequestWrapper extends HttpServletRequestWrapper {
	/**
	 * 생성자: HttpServletRequest 객체를 래핑합니다.
	 *
	 * @param servletRequest HttpServletRequest 객체
	 */
	public RequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	/**
	 * 파라미터 값을 가져와서 XSS 공격에 취약한 문자열을 제거하거나 변조하여 반환합니다.
	 *
	 * @param parameter 파라미터 이름
	 * @return 변조된 파라미터 값
	 */
	@Override
	public String getParameter(String parameter) {
		// 원본 파라미터 값을 가져옵니다.
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		// XSS 공격에 취약한 문자열을 제거하거나 변조합니다.
		return cleanXSS(value);
	}

	/**
	 * 헤더 값을 가져와서 XSS 공격에 취약한 문자열을 제거하거나 변조하여 반환합니다.
	 *
	 * @param name 헤더 이름
	 * @return 변조된 헤더 값
	 */
	@Override
	public String getHeader(String name) {
		// 원본 헤더 값을 가져옵니다.
		String value = super.getHeader(name);
		if (value == null) {
			return null;
		}
		// XSS 공격에 취약한 문자열을 제거하거나 변조합니다.
		return cleanXSS(value);
	}

	/**
	 * XSS 공격에 취약한 문자열을 제거하거나 변조하는 메서드.
	 *
	 * @param value 변조할 문자열
	 * @return 변조된 문자열
	 */
	private String cleanXSS(String value) {
		// HTML 특수 문자를 이스케이프 처리하여 XSS 공격 방지
		value = StringEscapeUtils.escapeHtml4(value);
		// 추가적인 특수 문자와 XSS 공격 패턴을 변조합니다.
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
		return value;
	}
}
