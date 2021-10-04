package com.mobifone.bigdata.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mobifone.bigdata.model.User;
import com.mobifone.bigdata.repo.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = getUserByUsername(username);
		
		return UserDetailsImpl.build(user);
	}

	public User getUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByUsername(username);
		
		if (user != null) {
			if (user.getStatus() == 0) {
				throw new UsernameNotFoundException("Tài khoản không hoạt động.");
			}
			return user;
		}
		throw new UsernameNotFoundException("Tài khoản không tồn tại.");

	}
	
}