package com.mobifone.bigdata.security;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.mobifone.bigdata.model.Partner;
import com.mobifone.bigdata.model.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Data;

@Data
@Component
@PropertySource("classpath:security.properties")
public class JwtConfig {
	private static final Logger logger = Logger.getLogger(JwtConfig.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpiration}")
	private int jwtExpiration;

	@Value("${app.prefix}")
	private String prefix;

	@Value("${app.header}")
	private String header;

	public String generateJwtToken(User user) {

		return Jwts.builder().setSubject((user.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String generateJwtToken(Partner part) {

		return Jwts.builder().setSubject((part.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: " + e.getMessage());
		}

		return false;
	}

}
