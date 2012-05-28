package com.n4systems.fieldid.ws.v1.resources.smartsearch;

public class ApiSmartSearchSuggestion implements Comparable<ApiSmartSearchSuggestion> {
	private String sid;
	private String type;
	private String identifier;
	private String customerRefNumber;
	private Integer fieldLength;

	public ApiSmartSearchSuggestion(String sid, String type, String identifier, String customerRefNumber, Integer fieldLength) {
		this.sid = sid;
		this.type = type;
		this.identifier = identifier;
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

    @Override
    public int compareTo(ApiSmartSearchSuggestion apiSmartSearchSuggestion) {
        return this.fieldLength.compareTo(apiSmartSearchSuggestion.fieldLength);
    }

    @Override
    public int hashCode() {
        return sid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ApiSmartSearchSuggestion)) return false;

        return this.sid.equals(((ApiSmartSearchSuggestion)o).sid);
    }

}
