package com.tnpfc.protean.API.DTO;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name="BANKRESPONSE")
@Table(name="BANKRESPONSE")
public class BankApiResponse {
	
	@Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bank_validation_seq_gen"
    )

    @SequenceGenerator(
            name = "bank_validation_seq_gen",
            sequenceName = "BANK_VALIDATION_SEQ",
            allocationSize = 1
    )
	
	@Column(name = "ID")
	private long ID;
	
	@Column(name="PROGRAM_ID")
	private String program_id;
	
	@Column(name="REQUEST_ID")
	private String request_id;
	
	@Column(name="IFSCCODE")
	private String ifdsccode;
	
	@Column(name="ACNUM")
	private String acnum;
	
	@Column(name="TXNTYPE")
	private String txntype;
	
	@Column(name="STATUS_BY")
	private String status_by;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "STATUS_DT", updatable = false)
	private Date status_dt;	
	
	@Column(name="STATUSCODE")
	private String statusCode;
	
	@Column(name="STATUS")	
	private String status;
	
	@Column(name="ACVALIDATIONSTATUS")
	private String acValidationStatus;
	
	@Column(name="MESSAGE")
	private String message;
	
	@Column(name="REQUESTID")
	private String requestId;
	
	@Column(name="RESPONSEID")
	private String responseId;
	
	@Column(name="NAMEATBANK")		
	private String nameAtBank;
	
	@Column(name="CUSTACCTNO")
	private String custAcctNo;
	
	@Column(name="CUSTIFSC")	
	private String custIfsc;
	
	@Column(name="BANKCOE")
	private String bankCode;
	
	@Column(name="METHODUSER")
	private String methodUsed;	
	
	@Column(name="UTR")
	private String utr;
	
	@Column(name="RESPONSESTRING")
	private String responsestring;
	
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAcValidationStatus() {
		return acValidationStatus;
	}
	public void setAcValidationStatus(String acValidationStatus) {
		this.acValidationStatus = acValidationStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getResponseId() {
		return responseId;
	}
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	public String getNameAtBank() {
		return nameAtBank;
	}
	public void setNameAtBank(String nameAtBank) {
		this.nameAtBank = nameAtBank;
	}
	public String getCustAcctNo() {
		return custAcctNo;
	}
	public void setCustAcctNo(String custAcctNo) {
		this.custAcctNo = custAcctNo;
	}
	public String getCustIfsc() {
		return custIfsc;
	}
	public void setCustIfsc(String custIfsc) {
		this.custIfsc = custIfsc;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getMethodUsed() {
		return methodUsed;
	}
	public void setMethodUsed(String methodUsed) {
		this.methodUsed = methodUsed;
	}
	public String getUtr() {
		return utr;
	}
	public void setUtr(String utr) {
		this.utr = utr;
	}
	
	public Date getStatus_dt() {
		return status_dt;
	}
	public void setStatus_dt(Date status_dt) {
		this.status_dt = status_dt;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getProgram_id() {
		return program_id;
	}
	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getIfdsccode() {
		return ifdsccode;
	}
	public void setIfdsccode(String ifdsccode) {
		this.ifdsccode = ifdsccode;
	}
	public String getAcnum() {
		return acnum;
	}
	public void setAcnum(String acnum) {
		this.acnum = acnum;
	}
	public String getTxntype() {
		return txntype;
	}
	public void setTxntype(String txntype) {
		this.txntype = txntype;
	}
	public String getStatus_by() {
		return status_by;
	}
	public void setStatus_by(String status_by) {
		this.status_by = status_by;
	}
	public String getResponsestring() {
		return responsestring;
	}
	public void setResponsestring(String responsestring) {
		this.responsestring = responsestring;
	}
	
	
	
	   
}
