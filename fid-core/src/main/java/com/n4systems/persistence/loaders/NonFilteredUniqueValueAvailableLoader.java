package com.n4systems.persistence.loaders;


public abstract class NonFilteredUniqueValueAvailableLoader extends Loader<Boolean> {

	private Long id;
	
	private String uniqueName;

	protected String getUniqueName() {
		return uniqueName;
	}

	public NonFilteredUniqueValueAvailableLoader setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName.trim();
		return this;
	}

	protected boolean isIdGiven() {
		return id != null;
	}
	
	protected Long getId() {
		return id;
	}

	public NonFilteredUniqueValueAvailableLoader setId(Long id) {
		this.id = id;
		return this;
	}
	

}
