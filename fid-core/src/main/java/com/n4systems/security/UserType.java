package com.n4systems.security;

import org.apache.commons.lang.StringUtils;

import java.util.*;


public enum UserType {
	ALL( "All", PermissionType.allPermissions() ),
	ADMIN( "Admin", PermissionType.allPermissions() ),
	SYSTEM( "System", PermissionType.allPermissions() ),
    FULL( "Administration", PermissionType.values() ),
    LITE( "Inspection", PermissionType.CreateEvent, PermissionType.EditEvent ),
    READONLY( "Reporting" /*no permissions*/ ),
    PERSON("Person" /*no permissions*/),
    USAGE_BASED("Usage Based", PermissionType.CreateEvent, PermissionType.EditEvent);

	private String label;
	private final Set<PermissionType> permissionSet = new HashSet<PermissionType>();
	
	UserType( String label, PermissionType...permissions ) {
		this.label = label;
		permissionSet.addAll(Arrays.asList(permissions));
	}
	
	public String getLabel() {
		return label;
	}

	public static UserType valueFromLabel(String name) {
		String label = StringUtils.trimToEmpty(name); 
		for (UserType type:values()) { 
			if(label.equalsIgnoreCase(type.getLabel())) {
				return type;
			}
		}
		return null;
	}	

	public static boolean hasPermission(UserType userType, PermissionType permission) { 
		return userType != null && userType.permissionSet.contains(permission);
	}		
	
	public static boolean hasPermission(UserType userType, int permission) {
		if (userType==null) { 
			return false;
		}
		for (Iterator<PermissionType> iterator = userType.permissionSet.iterator(); iterator.hasNext(); ) {
			PermissionType permissionType = iterator.next();
			if (permissionType.getValue()==permission) {
				return true;
			}
		}
		return false;
	}

	public static List<String> labelValues() {
		List<String> result = new ArrayList<String>();
		for (UserType type:values()) { 
			result.add(type.getLabel());
		}
		return result;
		
	}
		
}
