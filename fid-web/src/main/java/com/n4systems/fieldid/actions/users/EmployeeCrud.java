package com.n4systems.fieldid.actions.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class EmployeeCrud extends UserCrud {
	private static final long serialVersionUID = 1L;

	private Map<String, Boolean> userPermissions = new HashMap<String, Boolean>();
	protected List<ListingPair> permissions;
	
	public EmployeeCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}

	private void setupPermissions() {
		userPermissions = new HashMap<String, Boolean>();

		BitField permField = new BitField(user.getPermissions());
		for (ListingPair permission : getPermissions()) {
			userPermissions.put(permission.getId().toString(), permField.isSet(permission.getId().intValue()));
		}
	}
	
	@Override
	protected void testRequiredEntities(boolean existing) {
		super.testRequiredEntities(existing);
		if (existing && !user.isEmployee()) {
			throw new MissingEntityException("customer user was loaded for when an employee was expected.");
		}
	}
	
	@Override
	@SkipValidation
	public String doAdd() {
		
		String result = super.doAdd();
		setupPermissions();
		isEmployeeLimitReached();
		return result;
	}
	
	@Override
	@SkipValidation
	public String doEdit() {
		String result = super.doEdit();
		setupPermissions();
		return result;
	}
	
	@Override
	public String doShow() {
		setupPermissions();
		return super.doShow();
	}
		
	@SuppressWarnings("unchecked")
	public Map getUserPermissions() {
		return userPermissions;
	}

	@SuppressWarnings("unchecked")
	public void setUserPermissions(Map permissions) {
		this.userPermissions = permissions;
	}

	public List<ListingPair> getPermissions() {
		if (permissions == null) {
			permissions = ListHelper.intListableToListingPair(Permissions.getSystemUserPermissions());
		}
		return permissions;
	}
	
	public boolean getUserPermissionValue(Long id) {
		return userPermissions.get(id.toString());
	}

	@Override
	protected int processPermissions() {
		int permission = defaultPermissionValue();
		
		if (userPermissions != null && !user.isAdmin()) { // needed to
						// handle when
						// there is an
						// empty list
						// submitted.
			BitField perms = new BitField();
			/*
			* we could do this by simply converting the id's back to
			* strings and setting them on the field, however that would
			* unsafe, since we can't trust what's coming back. Instead, get
			* the list of permissions they were allowed to see in the first
			* place, and ensure our key is from that set
			*/
			int permValue;
			String permStr;
			for (ListingPair allowedPerm : getPermissions()) {
				permValue = allowedPerm.getId().intValue();
				permStr = allowedPerm.getId().toString();
				
				// if our permission map contains the permission id, and
				// it's value is true, add the permission
				if (userPermissions.containsKey(permStr) && userPermissions.get(permStr)) {
					perms.set(permValue);
				}
			}
				
			permission = perms.getMask();
			
		} 
		return permission;
	}

	
	private int defaultPermissionValue() {
		return (user.isAdmin()) ? Permissions.ADMIN : Permissions.NO_PERMISSIONS;
	}

	public boolean isEmployee() {
		return true;
	}

	@Override
	@FieldExpressionValidator(message="", key="error.owner_must_be_an_organization", expression="owner.internal == true")
	public BaseOrg getOwner() {
		return super.getOwner();
	}
	
	@RequiredFieldValidator(message="")
	public boolean isEmployeeLimitReached() {
		if (user.isNew() && getLimits().isEmployeeUsersMaxed()) {
			addActionError(getText("label.exceeded_your_employee_user_limit", new String[] { getLimits().getEmployeeUsersMax().toString() } ));
			return true;
		}
		return false;
	}

	

	
}
