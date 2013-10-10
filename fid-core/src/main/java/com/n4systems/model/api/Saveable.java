package com.n4systems.model.api;

import java.util.Map;

public interface Saveable {
	public boolean isNew();
	public Object getEntityId();
    public boolean isTranslated();
    public void setUntranslatedValue(String name, Object value);
    // gets the values that have been translated.   e.g. colour-->Red,   while calling getColour() would yield Rouge.     it is the original value before being translated
    public Map<String,Object> getTranslatedValues();
}
