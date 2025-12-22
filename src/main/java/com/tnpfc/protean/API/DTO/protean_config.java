package com.tnpfc.protean.API.DTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "PROTEAN_CONFIG")
@Table(name="PROTEAN_CONFIG")
public class protean_config {
	@Id
	@Column(name="CONFIG_TYPE")
	private String configtype;
	
	@Column(name="CONFIG_TOKEN")
	private String CONFIG_TOKEN;
	
	
}
