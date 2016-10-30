package ch.unibe.ese.team3.interceptors;

import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggingInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger("Compass logger");

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Principal user = request.getUserPrincipal();

		String userName = user != null ? user.getName() : "Anonymous";

		StringBuilder sb = new StringBuilder();

		Enumeration<String> parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				
				String paramValue = paramValues[i];
				sb.append(String.format("%s=%s", paramName, paramValue));
				sb.append(" ");
			}
		}

		String logEntry = String.format("%s %s { %s} %s", request.getMethod(), request.getRequestURI(), sb.toString(), userName);

		logger.info(logEntry);
		return true;
	}

}
