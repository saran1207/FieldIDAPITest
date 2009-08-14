package com.n4systems.model.inspectiontype;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.persistence.savers.Saver;

public class AssociatedInspectionTypeSaver extends Saver<AssociatedInspectionType> {

	
	@Override
	protected AssociatedInspectionType update(EntityManager em, AssociatedInspectionType entity) {
		throw new NotImplementedException("update is not valid on this relationship");
	}
	
	
}
