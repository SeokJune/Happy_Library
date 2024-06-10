package com.happy.library.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring의 HandlerInterceptor를 구현한 AuthCheckInterceptor 클래스 요청이 컨트롤러에 도달하기 전에 실행되어 인증을 체크하고 필요한 경우 처리를 수행합니다.
 */
public class AuthCheckInterceptor implements HandlerInterceptor {
	// Logger 인스턴스 생성
	private final Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * 컨트롤러의 핸들러 메서드가 호출되기 전에 호출되는 메서드입니다. 인증 정보를 확인하고, 필요한 경우 요청을 처리하지 않고 리다이렉트하거나 에러를 반환합니다.
	 *
	 * @param request HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @param handler 선택된 핸들러 객체
	 * @return true면 다음 인터셉터나 핸들러가 실행되고, false면 요청이 처리되지 않습니다.
	 * @throws Exception 예외가 발생할 경우
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 세션 가져오기 (있으면 존재하는 세션 반환, 없으면 null 반환)
		HttpSession session = request.getSession(false);

		// 세션 존재 여부 확인
		if (session != null) {
			// 요청 URI 가져오기
			String requestUri = request.getRequestURI();

			// 요청 URI가 루트 경로('/')인지 확인
			if ("/".equals(requestUri)) {
				// 루트 경로로 접근한 경우 로그를 남기고 루트로 리다이렉트
				logger.info("root path in");
				response.sendRedirect(request.getContextPath() + "/");
				response.flushBuffer();
				return false;
			}

			// 세션에 인증 정보("usrMnuAtrt" 속성)가 있는지 확인
			Object authInfo = session.getAttribute("usrMnuAtrt");
			if (authInfo != null) {
				// 인증 정보가 있으면 요청을 계속 처리
				return true;
			}
		}

		// HTTP 헤더에서 'X-Requested-With' 헤더 값을 가져와서 axios 요청인지 확인
		String axiosHeader = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(axiosHeader)) {
			// axios 요청인 경우 901 에러 코드 반환
			response.sendError(901);
			return false;
		}

		// 인증 정보가 없으면 요청을 계속 처리 (인증이 필요한 경우 추가 처리가 필요할 수 있음)
		return true;
	}

	/**
	 * 컨트롤러가 요청을 처리한 후, 뷰가 렌더링되기 전에 호출되는 메서드입니다. 여기에서 모델에 추가 데이터를 넣거나, 뷰 이름을 변경하는 등의 작업을 수행할 수 있습니다.
	 *
	 * @param request HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @param handler 선택된 핸들러 객체
	 * @param modelAndView ModelAndView 객체 (모델과 뷰 정보를 담고 있음)
	 * @throws Exception 예외가 발생할 경우
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// 현재는 특별히 할 작업이 없으므로 메서드를 비워둡니다.
	}

	/**
	 * 요청이 완료된 후 호출되는 메서드입니다. 여기에서 리소스 정리 등의 사후 작업을 수행할 수 있습니다.
	 *
	 * @param request HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @param handler 선택된 핸들러 객체
	 * @param ex 처리 중 발생한 예외 (있을 경우)
	 * @throws Exception 예외가 발생할 경우
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 현재는 특별히 할 작업이 없으므로 메서드를 비워둡니다.
	}
}