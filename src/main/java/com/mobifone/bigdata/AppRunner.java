package com.mobifone.bigdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {

		System.out.println(encoder.encode("123456a@"));
	
	}

}