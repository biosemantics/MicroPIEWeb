package edu.arizona.biosemantics.micropie.web.shared.model;

import java.io.Serializable;

public class BatchText implements Serializable {

	private static final long serialVersionUID = 1L;
	private String emailAddr;
	private String batchText;

	public BatchText() {
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

}
