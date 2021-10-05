package com.mobifone.bigdata.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mobifone.bigdata.model.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer>{
	
	@Query(value = "SELECT * FROM partner WHERE user_name = :user_name ;", nativeQuery = true)
	public Partner getPartner(String user_name);
}
