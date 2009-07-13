package com.n4systems.model;

import com.n4systems.model.api.Cleaner;
import com.n4systems.model.parents.AbstractEntity;

abstract public class AbstractEntityCleaner<T extends AbstractEntity> implements Cleaner<T> {

	public AbstractEntityCleaner() {}
	
	public void clean(T obj) {
		obj.setId(null);
		obj.setCreated(null);
		obj.setModified(null);
		obj.setModifiedBy(null);
	}

}
