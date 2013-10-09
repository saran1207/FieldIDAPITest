package com.n4systems.model.api;

import java.util.Map;

public interface Saveable {
	public boolean isNew();
	public Object getEntityId();
    public boolean isTranslated();
    public void setUntranslatedValue(String name, Object value);
    public Map<String,Object> getTranslatedValues();
}
