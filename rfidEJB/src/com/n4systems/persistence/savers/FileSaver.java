package com.n4systems.persistence.savers;

import com.n4systems.model.parents.EntityWithTenant;


public abstract class FileSaver<T extends EntityWithTenant> {
	public abstract void save();
	public abstract void remove();
	
}
