package com.n4systems.exceptions;

public class DuplicateRfidException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String userId;
	private String rfidNumber;
	
	public DuplicateRfidException() {}

	public DuplicateRfidException(String message, String userId, String rfidNumber) {
		super(message);
		setUserId(userId);
		setRfidNumber(rfidNumber);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}
}
