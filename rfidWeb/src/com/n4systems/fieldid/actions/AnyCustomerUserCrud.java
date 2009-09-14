package com.n4systems.fieldid.actions;

import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class AnyCustomerUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;
	
	public AnyCustomerUserCrud(User userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}
	
	public boolean getCustomerRequired() {
		return true;
	}
	
	@Override
	public boolean isCustomerUser() {
		return true;
	}
		
}
