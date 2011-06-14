package com.n4systems.security;





public enum PermissionType {	
	

	
	Tag("label.identify_permission", Permissions.Tag ), 				
	ManageSystemConfig("label.managesystemconfig_permission", Permissions.ManageSystemConfig),	
	ManageSystemUsers("label.managesystemusers_permission", Permissions.ManageSystemUsers),	
	ManageEndUsers("label.managecustomers_permission", Permissions.ManageEndUsers),		
	CreateEvent("label.createevent_permission",Permissions.CreateEvent),         
	EditEvent("label.editevent_permission",Permissions.EditEvent),           
	ManageJobs("label.managejobs_permission",Permissions.ManageJobs),
	ManageSafetyNetwork("label.managesafetynetwork_permission",Permissions.ManageSafetyNetwork), 
	AccessWebStore("label.accesswebstore_permission", Permissions.AccessWebStore), 
	// composite permissions....
	ADMIN(Integer.MAX_VALUE-1),	// set all bits. 
	SYSTEM(Integer.MAX_VALUE-1),// 
	CUSTOMER(0), 
	ALLEVENT(CreateEvent.getValue(), EditEvent.getValue() ),
	;		
		
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