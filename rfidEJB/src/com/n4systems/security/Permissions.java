package com.n4systems.security;

import java.util.ArrayList;
import java.util.List;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.api.Listable;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.SimpleListable;


public class Permissions {
	// Primary permissions
	public static final int TAG 				= 1 << 0;
	public static final int MANAGESYSTEMCONFIG	= 1 << 1;
	public static final int MANAGESYSTEMUSERS	= 1 << 2;
	public static final int MANAGEENDUSERS		= 1 << 3;
	public static final int CREATEINSPECTION	= 1 << 4;
	public static final int EDITINSPECTION		= 1 << 5;
	public static final int MANAGEJOBS			= 1 << 6;
	// NOTE: We can have a most 30 permissions (1 << 30) since we're using ints
	
	// Composite permissions
	public static final int ALL 				= Integer.MAX_VALUE;	// (2^31 - 1)
	public static final int ADMIN				= ALL;
	public static final int SYSTEM				= ALL;
	public static final int ALLINSPECTION		= CREATEINSPECTION | EDITINSPECTION;
	
	/** permissions visible for admins to select for system users */
	private static final int[] visibleSytemUserPermissions = { TAG, MANAGESYSTEMCONFIG, MANAGESYSTEMUSERS, MANAGEENDUSERS, CREATEINSPECTION, EDITINSPECTION, MANAGEJOBS };
	
	/** permissions visible for admins to select for customer users */
	private static final int[] visibleCustomerUserPermissions = { CREATEINSPECTION, EDITINSPECTION };
	
	/**
	 * @param permission a Primary permission (ie not composite)
	 * @return The label for a primary permission
	 */
	public static String getLabel(int permission) {
		String label = null;
		switch (permission) {
			case TAG:
				label = "label.tag_permission";
				break;
			case MANAGESYSTEMCONFIG:
				label = "label.managesystemconfig_permission";;
				break;
			case MANAGESYSTEMUSERS:
				label = "label.managesystemusers_permission";;
				break;
			case MANAGEENDUSERS:
				label = "label.manageendusers_permission";;
				break;
			case CREATEINSPECTION:
				label = "label.createinspection_permission";;
				break;
			case EDITINSPECTION:
				label = "label.editinspection_permission";;
				break;
			case MANAGEJOBS:
				label = "label.managejobs_permission";;
				break;
		}
		return label;
	}
	
	/**
	 * @return Returns the permission for the legacy permission name
	 */
	public static int getPermissionForLegacyName(String permissionName) {
		String safeName = permissionName.trim().toLowerCase();
		
		if (safeName.equals("tag")) {
			return TAG;
		} else if (safeName.equals("managesystemconfig")) {
			return MANAGESYSTEMCONFIG;
		} else if (safeName.equals("managesystemusers")) {
			return MANAGESYSTEMUSERS;
		} else if (safeName.equals("manageendusers")) {
			return MANAGEENDUSERS;
		} else if (safeName.equals("createinspection")) {
			return CREATEINSPECTION;
		} else if (safeName.equals("editinspection")) {
			return EDITINSPECTION;
		} else if (safeName.equals("managejobs")) {
			return MANAGEJOBS;
		} else {
			throw new InvalidPermission("Unknown permission name [" + permissionName + "]");
		}
	}
	
	/**
	 * @return A List of visible system user permissions.  displayName will be set to the permission label.
	 */
	public static List<Listable<Integer>> getSystemUserPermissions() {
		return createPermissionDisplayList(visibleSytemUserPermissions);
	}
	
	/**
	 * @return A List of visible customer user permissions.  displayName will be set to the permission label.
	 */
	public static List<Listable<Integer>> getCustomerUserPermissions() {
		return createPermissionDisplayList(visibleCustomerUserPermissions);
	}
	
	private static List<Listable<Integer>> createPermissionDisplayList(int ... perms) {
		List<Listable<Integer>> displayList = new ArrayList<Listable<Integer>>();
		for (int perm: perms) {
			displayList.add(new SimpleListable<Integer>(perm, getLabel(perm)));
		}
		return displayList;
	}
	
	/**
	 * @return True iff one or more perms is set on user.
	 */
	public static boolean hasOneOf(UserBean user, int ... perms) {
		BitField permField = new BitField(user.getPermissions());
		
		boolean hasAccess = false;
		for (int perm: perms) {
			if (permField.isSet(perm)) {
				hasAccess = true;
				break;
			}
		}
		return hasAccess;
	}
	
	/**
	 * @return True iff ALL perms are set on user.
	 */
	public static boolean hasAllOf(UserBean user, int ... perms) {
		BitField permField = new BitField(user.getPermissions());
		
		boolean hasAccess = true;
		for (int perm: perms) {
			if (!permField.isSet(perm)) {
				hasAccess = false;
				break;
			}
		}
		return hasAccess;
	}
	
	/**
	 * @return A new ArrayList<UserBean> of users passing hasOneOf(user, perms)
	 */
	public static List<UserBean> filterHasOneOf(List<UserBean> users, int ... perms) {
		List<UserBean> filteredUsers = new ArrayList<UserBean>();
		for (UserBean user: users) {
			if (hasOneOf(user, perms)) {
				filteredUsers.add(user);
			}
		}
		return filteredUsers;
	}
	
	/**
	 * @return A new ArrayList<UserBean> of users passing hasAllOf(user, perms)
	 */
	public static List<UserBean> filterHasAllOf(List<UserBean> users, int ... perms) {
		List<UserBean> filteredUsers = new ArrayList<UserBean>();
		for (UserBean user: users) {
			if (hasAllOf(user, perms)) {
				filteredUsers.add(user);
			}
		}
		return filteredUsers;
	}
}
