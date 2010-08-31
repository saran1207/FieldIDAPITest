package com.n4systems.model;

import com.n4systems.model.api.Cleaner;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;

public class LegacyBaseEntityCleaner<T extends LegacyBaseEntity> implements Cleaner<T> {

	public LegacyBaseEntityCleaner() {}
	
	public void clean(T obj) {
		obj.setUniqueID(null);
	}

}
