package com.n4systems.model.api;

public interface Exportable extends HasTenant {
	public void setGlobalId(String globalId);
	public String getGlobalId();
}
