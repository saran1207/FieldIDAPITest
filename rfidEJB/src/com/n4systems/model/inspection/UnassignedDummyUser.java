package com.n4systems.model.inspection;

import com.n4systems.model.api.Listable;

public class UnassignedDummyUser<Long> implements Listable<Long> {

	private Long id;
	private String displayName;
	
	public UnassignedDummyUser(){
		displayName="Unassigned";
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	


}
