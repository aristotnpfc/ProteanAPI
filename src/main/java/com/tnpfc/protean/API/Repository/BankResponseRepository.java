package com.tnpfc.protean.API.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnpfc.protean.API.DTO.BankApiResponse;

@Repository
public interface BankResponseRepository extends JpaRepository<BankApiResponse, Long>{
	
}
