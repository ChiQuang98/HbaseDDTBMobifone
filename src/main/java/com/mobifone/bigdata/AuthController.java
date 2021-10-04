package com.mobifone.bigdata;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobifone.bigdata.ldap.BlindSslSocketFactory;
import com.mobifone.bigdata.model.User;
import com.mobifone.bigdata.request.LoginRequest;
import com.mobifone.bigdata.response.LoginResponse;
import com.mobifone.bigdata.security.JwtConfig;
import com.mobifone.bigdata.service.UserDetailsImpl;
import com.mobifone.bigdata.service.UserDetailsServiceImpl;
import com.mobifone.bigdata.util.Constants;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static Logger logger = Logger.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	public static String dotEmail = "@mobifone.vn";

	@Autowired
	private JwtConfig jwtConfig;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) throws Exception {

		LoginResponse loginResponse = null;

		User user = userDetailsServiceImpl.getUserByUsername(request.getUsername());

		if (user != null) {

			if (user.getLogin_type() == Constants.LDAP) {
				logger.info("LOGIN LDAP");
				if (BlindSslSocketFactory.authentication(request.getUsername(), request.getPassword())
						|| BlindSslSocketFactory.authentication(request.getUsername() + dotEmail,
								request.getPassword())) {

					loginResponse = generateToken(user);

				} else {
					throw new BadCredentialsException("Mật khẩu không đúng");
				}
			} else if (user.getLogin_type() == Constants.LOGIN) {
				logger.info("LOGIN");
				authenticate(request.getUsername(), request.getPassword());
				loginResponse = generateToken(user);
			}
		}

		return ResponseEntity.ok(loginResponse);
	}
	
//	@PostMapping("/signin")
//	public ResponseEntity<?> signin( @RequestBody LoginRequest request) throws Exception {
//
//		LoginResponse loginResponse = new LoginResponse();
//		
//		FinPartner finPartner = finPartnerService.getFinPartner(request.getUsername(),request.getPassword());
//
//		loginResponse = generateToken(finPartner);
//
//		return ResponseEntity.ok(loginResponse);
//	}
	

	private Authentication authenticate(String username, String password) throws Exception {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return authentication;
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("Mật khẩu không đúng.", e);
		}
	}

	private LoginResponse generateToken(Object object) {

		LoginResponse loginResponse = null;

		User user = (User) object;

		String token = jwtConfig.generateJwtToken(user);

		UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(user.getUsername());

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		loginResponse = new LoginResponse(userDetails.getUsername(), token);

		return loginResponse;
	}

}