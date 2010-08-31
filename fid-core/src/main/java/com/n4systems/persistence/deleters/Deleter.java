package com.n4systems.persistence.deleters;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.Transaction;

public interface Deleter<T extends Saveable> {

	/** Removes an entity. */
	public void remove(T entity) throws EntityStillReferencedException;
	
	/** Removes an entity using an existing Transaction. */
	public void remove(Transaction transaction, T entity) throws EntityStillReferencedException;
	
}
