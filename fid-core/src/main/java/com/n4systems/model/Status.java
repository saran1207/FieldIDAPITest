/**
 * 
 */
package com.n4systems.model;

import com.n4systems.model.api.Listable;

public enum Status implements Listable<String> { 
	PASS("Pass", "label.pass"), FAIL("Fail", "label.fail"), NA("N/A", "label.na");

	private String displayName;
	private String label;
	
	Status(String displayName, String label ) {
		this.displayName = displayName;
		this.label = label;
	}
	
	public String getId() {
		return name();
	} 
	
	public String getDisplayName() {
		return displayName;
	}

	public String getLabel() {
		return label;
	}
	
	
}