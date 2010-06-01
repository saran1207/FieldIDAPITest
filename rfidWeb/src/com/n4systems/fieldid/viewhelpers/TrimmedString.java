package com.n4systems.fieldid.viewhelpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;


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

	public static List<TrimmedString> mapToTrimmedStrings(List<String> strings){
		List<TrimmedString> result = new ArrayList<TrimmedString>();
		for (String str : strings){
			result.add(new TrimmedString(str));
		}
		return result;
	}

	public static List<String> mapFromTrimmedStrings(List<TrimmedString> trimmed){
		List<String> strings = new ArrayList<String>();
		for (TrimmedString trimmedString : trimmed){
			if (trimmedString != null){
				strings.add(trimmedString.getTrimmedString());
			}
		}
		return strings;
	}
}
