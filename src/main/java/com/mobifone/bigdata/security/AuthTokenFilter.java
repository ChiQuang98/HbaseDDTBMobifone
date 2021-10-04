package com.mobifone.bigdata.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mobifone.bigdata.exception.ErrorDetail;
import com.mobifone.bigdata.exception.ExceptionConstant;
import com.mobifone.bigdata.service.UserDetailsServiceImpl;



public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtConfig jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = Logger.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				sendForbiddenMessage(request, response);
	            return;
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: " + e);
			sendForbiddenMessage(request, response);
            return;
		}

		filterChain.doFilter(request, response);
	}

	 private void sendForbiddenMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        // send forbidden message
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//	        ErrorDetail detail = new ErrorDetail();
//	        detail.setMessage("Unauthorized");
//	        detail.setStatus("Unauthorized");
	        
	        JSONObject jsonDataErr = new JSONObject();
	        jsonDataErr.put("message","Unauthorized");
	        
	        JSONObject jsonErr = new JSONObject();
	        jsonErr.put("status","Unauthorized");
	        jsonErr.put("data",jsonDataErr);
	        
	        response.setStatus(403);
	        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	        response.getOutputStream().write(jsonErr.toString().getBytes());
	    }
	
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(jwtUtils.getPrefix());

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(jwtUtils.getHeader())) {
			return headerAuth.substring(jwtUtils.getHeader().length(), headerAuth.length());
		}

		return null;
	}
}
