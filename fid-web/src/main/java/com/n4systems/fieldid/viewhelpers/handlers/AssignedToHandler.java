package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;

public class AssignedToHandler extends WebOutputHandler {

	public AssignedToHandler(WebContextProvider action) {
		super(action);
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}

	@Override
	public String handleWeb(Long entityId, Object value) {
		String assignedUser = translateValueToUser(value); 
		
		return renderUser(assignedUser);
	}

	private String translateValueToUser(Object value) {
		String assignedUser = null;
		if (value instanceof String) {
			assignedUser = (String)value;
		}
		return assignedUser;
	}

	private String renderUser(String assignedUser) {
		if (assignedUser == null || assignedUser.equals("")) {
			return contextProvider.getText("label.unassigned");
		}
		
		return assignedUser;
	}
}