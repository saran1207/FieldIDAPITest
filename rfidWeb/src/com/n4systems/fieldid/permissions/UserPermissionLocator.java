package com.n4systems.fieldid.permissions;


public class UserPermissionLocator {
	Class<?> actionClass;
	
	public UserPermissionLocator(Class<?> actionClass) {
		this.actionClass = actionClass;
	}
	
	
	protected UserPermissionFilter getClassPermissions() {
		UserPermissionFilter classFilter = actionClass.getAnnotation(UserPermissionFilter.class); 
		return classFilter;
	}

	protected UserPermissionFilter getMethodPermissions(String methodName) {
		try {
			return actionClass.getMethod(methodName).getAnnotation(UserPermissionFilter.class);
		} catch (Exception e) {	
			throw new RuntimeException("could not access the methods annotations", e);
		}
	}


	public int[] getActionPermissionRequirements(String methodName) {
		UserPermissionFilter filter = getMethodPermissions(methodName);
		
		if (filter == null) {
			filter = getClassPermissions();
		}
		
		return (filter != null) ? filter.userRequiresOnOf() : new int[]{};
	}

}
