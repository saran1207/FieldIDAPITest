package com.n4systems.model.inspectiontype;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.persistence.savers.Saver;

public class AssociatedEventTypeSaver extends Saver<AssociatedEventType> {

	
	@Override
	protected AssociatedEventType update(EntityManager em, AssociatedEventType entity) {
		throw new NotImplementedException("update is not valid on this relationship");
	}
	
	
}
