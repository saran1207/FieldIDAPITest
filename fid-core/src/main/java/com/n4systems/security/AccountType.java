package com.n4systems.security;

import java.util.HashSet;
import java.util.Set;


public enum AccountType {
	// FIXME DD : yuck.  depends on static definition in permissions class.   
	// should be defined as Permission enumeration. 
	Full("Full", Permissions.getVisibleSystemUserPermissions()),
	Lite("Lite", Permissions.getVisibleLiteUserPermissions()),							
	ReadOnly("Read-Only", Permissions.getVisibleReadOnlyPermissions());
	
	private final String name;
	private Set<Integer> permissionSet;

	 AccountType(String name, int... permissions) { 
		this.name=name;
		this.permissionSet = new HashSet<Integer>();
		for (int permission:permissions) { 
			permissionSet.add(permission);
		}
	}

	public String getName() {
		return name;
	}
	
	public static AccountType valueFromName(String name) {
		if (name==null) { 
			return null;
		}
		for (AccountType type:values()) { 
			if(name.equals(type.getName())) {
				return type;
			}
		}
		return null;
	}
	
	public boolean hasPermission(Integer permission) { 
		return permissionSet.contains(permission);
	}
}	