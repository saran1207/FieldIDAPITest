package com.n4systems.fieldid.actions.users;

import java.util.ArrayList;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.UserGroup;
import com.n4systems.util.UserType;

public class FilterUserTypesByGroupAction extends AbstractAction {

	private ArrayList<StringListingPair> userTypes;
	private String userGroup;

	public FilterUserTypesByGroupAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doFilterUserTypes() {

		userTypes = new ArrayList<StringListingPair>();
		if (userGroup.equals(UserGroup.EMPLOYEE.toString()) || userGroup.equals(UserGroup.ALL.toString())) {
			userTypes = getEmployeeUserTypes();
		}else if (userGroup.equals(UserGroup.CUSTOMER.toString())){
			userTypes = getCustomerUserTypes();
		}

		return SUCCESS;
	}

	public ArrayList<StringListingPair> getEmployeeUserTypes() {
		ArrayList<StringListingPair> userTypes = new ArrayList<StringListingPair>();
		for (int i = 0; i < UserType.values().length; i++) {
			if (!UserType.values()[i].equals(UserType.SYSTEM) && !UserType.values()[i].equals(UserType.ADMIN)) {
				userTypes.add(new StringListingPair(UserType.values()[i].name(), UserType.values()[i].getLabel()));
			}
		}
		return userTypes;
	}

	public ArrayList<StringListingPair> getCustomerUserTypes() {
		ArrayList<StringListingPair> userTypes = new ArrayList<StringListingPair>();
		for (int i = 0; i < UserType.values().length; i++) {
			if (!UserType.values()[i].equals(UserType.SYSTEM) && UserType.values()[i].equals(UserType.READONLY)) {
				userTypes.add(new StringListingPair(UserType.values()[i].name(), UserType.values()[i].getLabel()));
			}
		}
		return userTypes;
	}

	public String getUserGroup() {
		return userGroup;
	}
	
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public ArrayList<StringListingPair> getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(ArrayList<StringListingPair> userTypes) {
		this.userTypes = userTypes;
	}
}
