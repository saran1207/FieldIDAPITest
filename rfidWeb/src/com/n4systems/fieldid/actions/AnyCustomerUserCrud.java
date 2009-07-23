package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rfid.ejb.session.User;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class AnyCustomerUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;
	
	public AnyCustomerUserCrud( User userManager, CustomerManager customerManager, PersistenceManager persistenceManager ) {
		super( userManager, persistenceManager, customerManager );
	}
	
	public boolean getCustomerRequired() {
		return true;
	}
	
	public Collection<ListingPair> getDivisions() {
		if( divisions == null ) {
			divisions = new ArrayList<ListingPair>();
			
			Long customerId = ( getCustomerId() != null ) ? getCustomerId() : getCustomers().iterator().next().getId(); 
			
			divisions = customerManager.findDivisionsLP(customerId, getSecurityFilter());
		}
		
		return divisions;
	}
	
	public List<ListingPair> getPermissions() {
		if( permissions == null ){
			permissions = ListHelper.intListableToListingPair(Permissions.getCustomerUserPermissions());
		}
		return permissions;
	}
		
}
