package com.n4systems.fieldid.actions.users;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.security.Permissions;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class CustomerUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;
	
	public CustomerUserCrud( UserManager userManager, PersistenceManager persistenceManager ) {
		super(userManager, persistenceManager);
	}
	
	@Override
	protected void testRequiredEntities(boolean existing) {
		super.testRequiredEntities(existing);
		if (existing && user.isEmployee()) {
			addActionErrorText("error.user_should_be_for_a_customer");
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

}
