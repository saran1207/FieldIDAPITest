package com.n4systems.model.orgs;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;


/**
 * This is an ugly hack to keep the Java 8 compiler for going off the rails...
 */
public class FindOrCreateDivisionOrgHandler extends FindOrCreateExternalDivisionOrgHandler{
	public FindOrCreateDivisionOrgHandler(ListLoader<DivisionOrg> loader, Saver<? super DivisionOrg> saver) {
		super(loader, saver);
	}
	
	
	@Override
	protected DivisionOrg createOrg(CustomerOrg parent, String name, String code) {
		DivisionOrg division = new DivisionOrg();
				
		division.setTenant(parent.getTenant());
		division.setParent(parent);
		division.setCode(code);
		division.setName(name);
		
		return division;
	}


	
	
}
