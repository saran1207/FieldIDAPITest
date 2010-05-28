package com.n4systems.fieldid.viewhelpers;

public class TrimmedString {

	private final String trimmedString;

	public TrimmedString(String trimmedString) {
		super();
		
		this.trimmedString = (trimmedString != null) ? trimmedString.trim() : "";
	}

	public String getTrimmedString() {
		return trimmedString;
	}
	
	public boolean isEmpty() {
		return trimmedString.isEmpty();
	}
	
	@Override
	public String toString() {
		return trimmedString;
	}
	
	@Override
	public boolean equals(Object obj) {
		return trimmedString.equals(obj);
	}

	@Override
	public int hashCode() {
		return trimmedString.hashCode();
	}
	
}
