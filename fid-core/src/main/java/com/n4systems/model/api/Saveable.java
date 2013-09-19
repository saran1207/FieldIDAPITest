package com.n4systems.model.api;

public interface Saveable {
	public boolean isNew();
	public Object getEntityId();
    public boolean isTranslated();
    public void setTranslated(boolean translated);
}
