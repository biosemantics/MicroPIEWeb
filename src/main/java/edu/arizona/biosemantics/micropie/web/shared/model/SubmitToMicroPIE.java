package edu.arizona.biosemantics.micropie.web.shared.model;

import java.io.Serializable;

public class SubmitToMicroPIE implements Serializable {

	private static final long serialVersionUID = 1L;
	private String emailAddr;
	private String batchText;
	private String returnMsg;

	public SubmitToMicroPIE() {
	};

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setBatchText(String batchText) {
		this.batchText = batchText;
	}

	public String getBatchText() {
		return batchText;
	}
	
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

}
