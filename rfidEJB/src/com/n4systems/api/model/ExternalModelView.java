package com.n4systems.api.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class ExternalModelView implements Serializable {
	
	public ExternalModelView() {}
	
	public abstract String getGlobalId();
	
	public abstract void setGlobalId(String globalId);
	
	public boolean isNew() {
		return (getGlobalId() == null);
	}
}
