package com.n4systems.security;

import java.util.ArrayList;
import java.util.List;


import com.n4systems.model.api.Listable;
import com.n4systems.model.user.User;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.SimpleListable;


public class Permissions {
	
	
	
	// Primary permissions
	public static final int Tag 				= 1 << 0;
	public static final int ManageSystemConfig	= 1 << 1;
	public static final int ManageSystemUsers	= 1 << 2;
	public static final int ManageEndUsers		= 1 << 3;
	public static final int CreateEvent         = 1 << 4;
	public static final int EditEvent           = 1 << 5;
	public static final int ManageJobs			= 1 << 6;
	public static final int ManageSafetyNetwork = 1 << 7;
	public static final int AccessWebStore		= 1 << 8;
	// NOTE: We can have a most 30 permissions (1 << 30) since we're using ints
	
	// Composite permissions
	public static final int NO_PERMISSIONS 		= 0;
	public static final int ALL 				= Integer.MAX_VALUE;	// (2^31 - 1)
	public static final int ADMIN				= ALL;
	public static final int SYSTEM				= ALL;
	public static final int CUSTOMER			= NO_PERMISSIONS;
	public static final int ALLEVENT = CreateEvent | EditEvent;
	
	
	
	/** permissions visible for admins to select for system users */
	private static final int[] visibleSytemUserPermissions = { Tag, ManageSystemConfig, ManageSystemUsers, ManageEndUsers, CreateEvent, EditEvent, ManageJobs, ManageSafetyNetwork, AccessWebStore };

	private static final int[] visibleLiteUserPermissions = { CreateEvent, EditEvent };

	
	/**
	 * @param permission a Primary permission (ie not composite)
	 * @return The label for a primary permission
	 */
	public static String getLabel(int permission) {
		String label = null;
		switch (permission) {
			case Tag:
				label = "label.identify_permission";
				break;
			case ManageSystemConfig:
				label = "label.managesystemconfig_permission";;
				break;
			case ManageSystemUsers:
				label = "label.managesystemusers_permission";;
				break;
			case ManageEndUsers:
				label = "label.managecustomers_permission";;
				break;
			case CreateEvent:
				label = "label.createevent_permission";;
				break;
			case EditEvent:
				label = "label.editevent_permission";;
				break;
			case ManageJobs:
				label = "label.managejobs_permission";;
				break;
			case ManageSafetyNetwork:
				label = "label.managesafetynetwork_permission";
				break;
			case AccessWebStore:
				label = "label.accesswebstore_permission";
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
			return Tag;
		} else if (safeName.equals("managesystemconfig")) {
			return ManageSystemConfig;
		} else if (safeName.equals("managesystemusers")) {
			return ManageSystemUsers;
		} else if (safeName.equals("manageendusers")) {
			return ManageEndUsers;
		} else if (safeName.equals("createevent")) {
			return CreateEvent;
		} else if (safeName.equals("editevent")) {
			return EditEvent;
		} else if (safeName.equals("managejobs")) {
			return ManageJobs;
		} else if (safeName.equals("accesswebstore")) {
			return AccessWebStore;
		} else if (safeName.equals("managesafetynetwork")) {
			return ManageSafetyNetwork;
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
	
	public static List<Listable<Integer>> getLiteUserPermissions() {
		return createPermissionDisplayList(visibleLiteUserPermissions);
	}
	
	public static int[] getVisibleSystemUserPermissions() {
		return visibleSytemUserPermissions;
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
