package com.n4systems.security;

import com.n4systems.model.api.Listable;
import com.n4systems.model.user.User;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.SimpleListable;

import java.util.ArrayList;
import java.util.List;


public class Permissions {
			
	// Primary permissions
	public static final int TAG = 1<<0;  // aka IdentifyAssets
	public static final int MANAGE_SYSTEM_CONFIG = 1<<1;
	public static final int MANAGE_SYSTEM_USERS = 1<<2;
	public static final int MANAGE_END_USERS = 1<<3;  // aka ManageJobSites or Customers.
	public static final int CREATE_EVENT = 1<<4;
	public static final int EDIT_EVENT = 1<<5;
	public static final int MANAGE_JOBS = 1<<6;
	public static final int MANAGE_SAFETY_NETWORK = 1<<7;
	public static final int EDIT_ASSET_DETAILS = 1<<8;

	// Composite permissions
	public static final int NO_PERMISSIONS 		= 0;
	public static final int ALL 				= Integer.MAX_VALUE;	// (2^31 - 1)
	public static final int ADMIN				= ALL;
	public static final int SYSTEM				= ALL;
	public static final int CUSTOMER			= NO_PERMISSIONS;
	public static final int ALLEVENT = CREATE_EVENT | EDIT_EVENT;
	
	/** permissions visible for admins to select for system users */
	private static final int[] VISIBLE_SYSTEM_USER_EVENT_PERMISSIONS = {TAG, MANAGE_SYSTEM_CONFIG, MANAGE_SYSTEM_USERS, MANAGE_END_USERS, CREATE_EVENT, EDIT_EVENT, MANAGE_JOBS, MANAGE_SAFETY_NETWORK};

	private static final int[] VISIBLE_LITE_USER_EVENT_PERMISSIONS = {CREATE_EVENT, EDIT_EVENT};
	
	private static final int[] VISIBLE_READ_ONLY_EVENT_PERMISSIONS = {EDIT_ASSET_DETAILS};

	
	/**
	 * @param permissionValue a Primary permission (ie not composite)
	 * @return The label for a primary permission
	 */
	public static String getLabel(int permissionValue) {
		return PermissionType.getLabelForPermissionValue(permissionValue);
	}
	
	/**
	 * @return Returns the permission for the legacy permission name
	 */
	public static int getPermissionForLegacyName(String permissionName) {
		String safeName = permissionName.trim().toLowerCase();
		
		if (safeName.equals("tag")) {
			return TAG;
		} else if (safeName.equals("managesystemconfig")) {
			return MANAGE_SYSTEM_CONFIG;
		} else if (safeName.equals("managesystemusers")) {
			return MANAGE_SYSTEM_USERS;
		} else if (safeName.equals("manageendusers")) {
			return MANAGE_END_USERS;
		} else if (safeName.equals("createevent")) {
			return CREATE_EVENT;
		} else if (safeName.equals("editevent")) {
			return EDIT_EVENT;
		} else if (safeName.equals("managejobs")) {
			return MANAGE_JOBS;
		} else if (safeName.equals("managesafetynetwork")) {
			return MANAGE_SAFETY_NETWORK;
		} else if (safeName.equals("editassetdetails")) {
			return EDIT_ASSET_DETAILS;
		} else {
			throw new InvalidPermission("Unknown permission name [" + permissionName + "]");
		}
	}
	
	/**
	 * @return A List of visible system user permissions.  displayName will be set to the permission label.
	 */
	public static List<Listable<Integer>> getSystemUserPermissions() {
		return createPermissionDisplayList(VISIBLE_SYSTEM_USER_EVENT_PERMISSIONS);
	}
	
	public static List<Listable<Integer>> getLiteUserPermissions() {
		return createPermissionDisplayList(VISIBLE_LITE_USER_EVENT_PERMISSIONS);
	}
	
	public static int[] getVisibleSystemUserEventPermissions() {
		return VISIBLE_SYSTEM_USER_EVENT_PERMISSIONS;
	}
	
	public static int[] getVisibleLiteUserEventPermissions() {
		return VISIBLE_LITE_USER_EVENT_PERMISSIONS;
	}
	
	public static int[] getVisibleReadOnlyEventPermissions() {
		return VISIBLE_READ_ONLY_EVENT_PERMISSIONS;
	}
	
	private static List<Listable<Integer>> createPermissionDisplayList(int ... perms) {
		List<Listable<Integer>> displayList = new ArrayList<Listable<Integer>>();
		for (int perm: perms) {
			displayList.add(new SimpleListable<Integer>(perm, getLabel(perm)));
		}
		return displayList;
	}
	
	/**
	 * @return True iff one or more perms is set in permissions.
	 */
	public static boolean hasOneOf(int permissions, int ... perms) {
		BitField permField = new BitField(permissions);
		
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
	 * @return True iff one or more perms is set on user.
	 */
	public static boolean hasOneOf(User user, int ... perms) {
		return hasOneOf(user.getPermissions(), perms);
	}
	
	/**
	 * @return True iff ALL perms are set on user.
	 */
	public static boolean hasAllOf(User user, int ... perms) {
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
	
	public static List<User> filterHasOneOf(List<User> users, int ... perms) {
		List<User> filteredUsers = new ArrayList<User>();
		for (User user: users) {
			if (hasOneOf(user, perms)) {
				filteredUsers.add(user);
			}
		}
		return filteredUsers;
	}
	
	
	public static List<User> filterHasAllOf(List<User> users, int ... perms) {
		List<User> filteredUsers = new ArrayList<User>();
		for (User user: users) {
			if (hasAllOf(user, perms)) {
				filteredUsers.add(user);
			}
		}
		return filteredUsers;
	}

}
