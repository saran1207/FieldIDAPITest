package com.n4systems.model.eventtype;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.persistence.savers.Saver;

import javax.persistence.EntityManager;

public class AssociatedEventTypeSaver extends Saver<AssociatedEventType> {

	
	@Override
	public AssociatedEventType update(EntityManager em, AssociatedEventType entity) {
		throw new NotImplementedException("update is not valid on this relationship");
	}
	
	
}
