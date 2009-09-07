package com.n4systems.fieldid.actions;

import java.util.List;

import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class AnyCustomerUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;
	
	public AnyCustomerUserCrud(User userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}
	
	public boolean getCustomerRequired() {
		return true;
	}
	
	public List<ListingPair> getPermissions() {
		if( permissions == null ){
			permissions = ListHelper.intListableToListingPair(Permissions.getCustomerUserPermissions());
		}
		return permissions;
	}
		
}
