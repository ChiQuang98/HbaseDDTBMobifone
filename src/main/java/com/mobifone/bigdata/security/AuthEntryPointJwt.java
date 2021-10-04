package com.mobifone.bigdata.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.mobifone.bigdata.exception.ApplicationException;
import com.mobifone.bigdata.exception.ErrorDetail;
import com.mobifone.bigdata.exception.ExceptionConstant;


@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = Logger.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: " + authException.getMessage());
	}

}