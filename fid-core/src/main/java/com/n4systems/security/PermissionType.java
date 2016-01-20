package com.n4systems.security;





public enum PermissionType {	
	

	
	Tag("label.identify_permission", Permissions.TAG),
	ManageSystemConfig("label.managesystemconfig_permission", Permissions.MANAGE_SYSTEM_CONFIG),
	ManageSystemUsers("label.managesystemusers_permission", Permissions.MANAGE_SYSTEM_USERS),
	ManageEndUsers("label.managecustomers_permission", Permissions.MANAGE_END_USERS),
	CreateEvent("label.createevent_permission",Permissions.CREATE_EVENT),
	EditEvent("label.editevent_permission",Permissions.EDIT_EVENT),
	ManageJobs("label.managejobs_permission",Permissions.MANAGE_JOBS),
	ManageSafetyNetwork("label.managesafetynetwork_permission",Permissions.MANAGE_SAFETY_NETWORK),
	EditAssetDetails("label.editassetdetails_permission", Permissions.EDIT_ASSET_DETAILS),
	// composite permissions....
	ADMIN(Integer.MAX_VALUE-1),	// set all bits. 
	SYSTEM(Integer.MAX_VALUE-1),// 
	CUSTOMER(0), 
	ALLEVENT(CreateEvent.getValue(), EditEvent.getValue() );
		
	private int value;
	private String label;
	
	PermissionType(int...values) { 
		this("", values );
	}
	
	PermissionType(String label, int... values) {
		for (int value:values) { 
			this.value = this.value | (value);			
		}
		this.label = label;
	}

	public int getValue() {
		return value;
	}
	
	public String getLabel() { 
		return label;		
	}

	public static String getLabelForPermissionValue(int permissionValue) {
		for (PermissionType permission:values()) { 
			if (permission.getValue()==permissionValue) { 
				return permission.getLabel();
			}
		}
		
		return null;
	}

	public static PermissionType[] allPermissions() {
		return values();
	}

}	