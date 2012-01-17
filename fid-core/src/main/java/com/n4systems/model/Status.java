/**
 * 
 */
package com.n4systems.model;

import java.util.EnumSet;

import com.n4systems.model.api.Listable;

public enum Status implements Listable<String> {
	
	
	PASS("Pass", "label.pass"), FAIL("Fail", "label.fail"), NA("N/A", "label.na");

	public static final EnumSet<Status> ALL = EnumSet.allOf(Status.class);
	
	private String displayName;
	private String label;
	
	Status(String displayName, String label ) {
		this.displayName = displayName;
		this.label = label;
	}
	
	@Override
	public String getId() {
		return name();
	} 
	
	@Override
	public String getDisplayName() {
		return displayName;
	}

	public String getLabel() {
		return label;
	}	
	
}