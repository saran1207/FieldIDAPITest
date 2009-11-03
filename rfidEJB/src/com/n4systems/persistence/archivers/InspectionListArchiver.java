package com.n4systems.persistence.archivers;

import java.util.Set;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Inspection;

public class InspectionListArchiver extends EntityListArchiver<Inspection> {
	
	public InspectionListArchiver(Set<Long> ids, UserBean modifyUser) {
		super(Inspection.class, ids, modifyUser);
	}

	public InspectionListArchiver(Set<Long> ids) {
		super(Inspection.class, ids);
	}

}
