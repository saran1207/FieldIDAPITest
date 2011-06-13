package com.n4systems.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public enum UserType {
	ALL( "All", PermissionType.allPermissions() ),
	ADMIN( "Admin", PermissionType.allPermissions() ),
	SYSTEM( "System", PermissionType.allPermissions() ),
	FULL( "Full", PermissionType.values() ),
	LITE( "Lite", PermissionType.CreateEvent, PermissionType.EditEvent ),
	READONLY( "Read-Only" /*no permissions*/ );

	
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
		if (name==null) { 
			return null;
		}
		name = name.trim();
		for (UserType type:values()) { 
			if(name.equalsIgnoreCase(type.getLabel())) {
				return type;
			}
		}
		return null;
	}
	

	public boolean hasPermission(PermissionType permission) { 
		return permissionSet.contains(permission);
	}		
	
	public boolean hasPermission(int permission) {
		for (Iterator<PermissionType> iterator = permissionSet.iterator(); iterator.hasNext(); ) {
			PermissionType permissionType = iterator.next();
			if (permissionType.getValue()==permission) {
				return true;
			}
		}
		return false;
	}
		
}
