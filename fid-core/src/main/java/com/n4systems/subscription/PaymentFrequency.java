package com.n4systems.subscription;

public enum PaymentFrequency {

	Monthly("Monthly"),
	Quarterly("Quarterly"),
	BiAnnually("Bi-Annually"),
	Annually("Annually"),
	Upfront("Upfront");
	
	private String code;
	
	private PaymentFrequency(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
