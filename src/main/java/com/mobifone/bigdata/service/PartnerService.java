package com.mobifone.bigdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobifone.bigdata.model.Partner;
import com.mobifone.bigdata.repo.PartnerRepository;

@Service
public class PartnerService {

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private PartnerRepository partnerRepo;

	public Partner getPartner( String username, String password) throws Exception {
		Partner finPartner = partnerRepo.getPartner(username);
		if (finPartner != null) {
			if (finPartner.getStatus() == 0) {
				throw new UsernameNotFoundException("Tài khoản không hoạt động.");
			} else {

				boolean result = encoder.matches(password, finPartner.getPassword());

				System.out.println("result : " + result);

				if (!result) {
					throw new Exception("Mật khẩu không đúng.");
				}
				return finPartner;
			}

		}
		throw new UsernameNotFoundException("Tài khoản không tồn tại.");
	}
}
