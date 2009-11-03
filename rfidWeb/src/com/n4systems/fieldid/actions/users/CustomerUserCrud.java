package com.n4systems.fieldid.actions.users;

import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class CustomerUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;
	
	public CustomerUserCrud( User userManager, PersistenceManager persistenceManager ) {
		super(userManager, persistenceManager);
	}
	
	@Override
	protected void testRequiredEntities(boolean existing) {
		super.testRequiredEntities(existing);
		if (existing && user.isEmployee()) {
			addActionErrorText("error.");
			throw new MissingEntityException("employee user was loaded for when a customer user was expected.");
		}
	}
	
	@Override
	protected int processPermissions() {
		return Permissions.CUSTOMER;
	}


	@Override
	public boolean isEmployee() {
		return false;
	}
	
	@Override
	@FieldExpressionValidator(message="", key="error.owner_be_a_customer_or_division", expression="owner.external == true")
	public BaseOrg getOwner() {
		return super.getOwner();
	}

}
