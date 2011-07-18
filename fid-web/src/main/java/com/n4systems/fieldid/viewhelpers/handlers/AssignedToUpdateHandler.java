/**
 * 
 */
package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.user.User;

public class AssignedToUpdateHandler extends WebOutputHandler {
	private static final String NO_ASSIGNMENT = "";
	private WebOutputHandler assignedToHandler;

	public AssignedToUpdateHandler(WebContextProvider action) {
		super(action);
		assignedToHandler = new AssignedToHandler(action);
	}
	
	public void setAssignToHandler(WebOutputHandler assignedToHandler) {
		this.assignedToHandler = assignedToHandler;
		
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}

	@Override
	public String handleWeb(Long entityId, Object value) {
		if (value instanceof AssignedToUpdate) {
			AssignedToUpdate update = (AssignedToUpdate)value;
            User assignedUser = update.getAssignedUser();
            return assignedToHandler.handleWeb(entityId, assignedUser == null ? null : assignedUser.getDisplayName());
		}
		return NO_ASSIGNMENT;
	}
}