package com.n4systems.fieldid.actions.users;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.security.Permissions;
import com.n4systems.util.UserType;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.ReadOnlyUser)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class ReadOnlyUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;
	
	public ReadOnlyUserCrud( UserManager userManager, PersistenceManager persistenceManager ) {
		super(userManager, persistenceManager);
	}
	
	@Override
	protected void testRequiredEntities(boolean existing) {
		super.testRequiredEntities(existing);
		if (existing && !user.isReadOnly()) {
			throw new MissingEntityException("another user was loaded for when a read-only user was expected.");
		}
	}
	
	@Override
	public String doCreate(){
		testRequiredEntities(false);
		user.setUserType(UserType.READONLY);
		save();
		return SUCCESS;
	}
	
	@Override
	protected int processPermissions() {
		return Permissions.CUSTOMER;
	}

	@Override
	public boolean isFullUser(){
		return false;
	}
	
	@Override
	public boolean isEmployee() {
		return false;
	}

	@Override
	public boolean isLiteUser() {
		return false;
	}

	@Override
	public boolean isReadOnlyUser() {
		return true;
	}	

}
