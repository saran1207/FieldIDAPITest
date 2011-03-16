/**
 * 
 */
package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.event.AssignedToUpdate;

public class AssignedToUpdateHandler extends WebOutputHandler {
	private static final String NO_ASSIGNMENT = "";
	private WebOutputHandler assignedToHandler;

	public AssignedToUpdateHandler(AbstractAction action) {
		super(action);
		assignedToHandler = new AssignedToHandler(action);
	}
	
	public void setAssignToHandler(WebOutputHandler assignedToHandler) {
		this.assignedToHandler = assignedToHandler;
		
	}

	public Object handleExcel(Long entityId, Object value) {
		return null;
	}

	@Override
	public String handleWeb(Long entityId, Object value) {
		if (value instanceof AssignedToUpdate) {
			AssignedToUpdate update = (AssignedToUpdate)value;
			return assignedToHandler.handleWeb(entityId, update.getAssignedUser().getDisplayName());
		}
		return NO_ASSIGNMENT;
	}
}