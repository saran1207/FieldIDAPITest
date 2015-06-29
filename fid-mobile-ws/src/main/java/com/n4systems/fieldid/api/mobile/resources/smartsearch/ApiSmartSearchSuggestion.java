package com.n4systems.fieldid.api.mobile.resources.smartsearch;

public class ApiSmartSearchSuggestion {
	private String sid;
	private String type;
	private String identifier;
	private String customerRefNumber;
	private Integer fieldLength;
    private String rfidNumber;

    public ApiSmartSearchSuggestion(String sid, String type, String identifier, String rfidNumber,String customerRefNumber, Integer fieldLength) {
		this.sid = sid;
		this.type = type;
		this.identifier = identifier;
        this.rfidNumber = rfidNumber;
		this.customerRefNumber = customerRefNumber;
		this.fieldLength = fieldLength;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

    public String getRfidNumber() { return rfidNumber; }

    public void setRfidNumber(String rfidNumber) { this.rfidNumber = rfidNumber; }

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	public Integer getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}
}
