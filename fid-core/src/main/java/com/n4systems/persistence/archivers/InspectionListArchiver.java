package com.n4systems.persistence.archivers;

import java.util.Set;


import com.n4systems.model.Inspection;
import com.n4systems.model.user.User;

public class InspectionListArchiver extends EntityListArchiver<Inspection> {
	
	public InspectionListArchiver(Set<Long> ids, User modifyUser) {
		super(Inspection.class, ids, modifyUser);
	}

	public InspectionListArchiver(Set<Long> ids) {
		super(Inspection.class, ids);
	}

}
