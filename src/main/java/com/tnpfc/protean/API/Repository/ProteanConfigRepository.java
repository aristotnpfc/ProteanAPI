package com.tnpfc.protean.API.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnpfc.protean.API.DTO.protean_config;

@Repository
public interface ProteanConfigRepository extends JpaRepository<protean_config,String>{
	 protean_config findByConfigtype(String configtype);
}
