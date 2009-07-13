package com.n4systems.fieldid.actions;

import java.util.List;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;
import rfid.web.helper.Constants;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.Customer;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.UserType;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class DefinedCustomerUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;
	
	private Long customer;
	
	public DefinedCustomerUserCrud( User userManager, CustomerManager customerManager, PersistenceManager persistenceManager ) {
		super( userManager, persistenceManager,  customerManager );
	}
	
	public String doSave() {
		user.setR_EndUser( customer );
		return super.doSave();
	}
	
	public Pager<UserBean> getPage() {
		if( page == null ) {
			page = userManager.getUsers( getSecurityFilter(), true, getCurrentPage().intValue(),	Constants.PAGE_SIZE, "", UserType.CUSTOMERS, getCustomerBean() );
		}
		return page;
	}
	
	public Long getCustomer() {
		return customer;
	}
	
	public void setCustomer( Long customer ) {
		this.customer = customer;
	}
	
	public Customer getCustomerBean() {
		return customerManager.findCustomer(getCustomer(), getSecurityFilter());
	}
	
	public boolean getCustomerOnlyAdd() { 
		return true;
	}
	
	public List<ListingPair> getPermissions() {
		if( permissions == null ) {
			permissions = ListHelper.intListableToListingPair(Permissions.getCustomerUserPermissions());
		}
		return permissions;
	}

	
}
