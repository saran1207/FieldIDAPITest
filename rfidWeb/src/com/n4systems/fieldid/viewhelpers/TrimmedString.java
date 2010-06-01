package com.n4systems.fieldid.viewhelpers;

import org.apache.commons.lang.builder.EqualsBuilder;

import sun.security.util.Cache.EqualByteArray;

public class TrimmedString{

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
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return trimmedString.hashCode();
	}
	
	public int length(){
		return trimmedString.length();
	}

	
}
