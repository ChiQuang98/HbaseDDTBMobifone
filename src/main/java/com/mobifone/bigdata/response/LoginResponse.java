package com.mobifone.bigdata.response;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String jwtToken;
	
	public LoginResponse(String username, String jwtToken) {
			this.username = username;
			this.jwtToken = "Bearer " + jwtToken;
	}
	
	@Override
	public String toString() {
		try {
			return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

}
