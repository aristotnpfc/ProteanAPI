package com.tnpfc.protean.API.DTO;

public class EmailPayLoad {
	
	private String data;
    private String version;
    private String symmetricKey;
    private String hash;
    private String timestamp;
    private String requestId;
	
    
    
    public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSymmetricKey() {
		return symmetricKey;
	}
	public void setSymmetricKey(String symmetricKey) {
		this.symmetricKey = symmetricKey;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
    
    

}
