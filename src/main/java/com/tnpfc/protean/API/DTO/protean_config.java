package com.tnpfc.protean.API.DTO;

import java.util.Date;

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
	
	@Column(name="APIKEY")
	private String apikey;
	
	@Column(name="SECURITY_KEY")
	private String securitykey;

	@Column(name="client_id")
	private String client_id;
	
	@Column (name="access_token")
	private String access_token;
	
	@Column (name="application_name")
	private String application_name;
	
	@Column (name="status")
	private String status;
	
	@Column (name="status_dt")
	private Date status_dt;
	
	
	
	public String getConfigtype() {
		return configtype;
	}

	public void setConfigtype(String configtype) {
		this.configtype = configtype;
	}

	

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getSecuritykey() {
		return securitykey;
	}

	public void setSecuritykey(String securitykey) {
		this.securitykey = securitykey;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getApplication_name() {
		return application_name;
	}

	public void setApplication_name(String application_name) {
		this.application_name = application_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatus_dt() {
		return status_dt;
	}

	public void setStatus_dt(Date status_dt) {
		this.status_dt = status_dt;
	}	
	
	
	
	
	
}
