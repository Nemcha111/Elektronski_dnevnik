package com.iktpreobuka.elektronski_dnevnik_projekat.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@Component
//public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		setRealmName("DeveloperStack");
//		super.afterPropertiesSet();
//	}
//
//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
//			throws IOException, ServletException {
//		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
//
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		PrintWriter writer = response.getWriter();
//		writer.println("HTTP Status 401 -" + authEx.getMessage());
//	}
//
//}

@Component
public class AuthenticationEntryPoint  extends BasicAuthenticationEntryPoint {

	@Override
	public void afterPropertiesSet()  throws Exception {
		setRealmName("DeveloperStack");
		super.afterPropertiesSet();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
			throws IOException, ServletException {
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
	

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status 401 -" + authEx.getMessage());
	}
}
