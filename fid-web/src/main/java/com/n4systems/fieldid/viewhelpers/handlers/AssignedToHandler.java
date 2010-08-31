package com.n4systems.fieldid.viewhelpers.handlers;
        
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.user.User;

public class AssignedToHandler extends WebOutputHandler {
	private static final User UNASSIGNED_USER = null;

	public AssignedToHandler(AbstractAction action) {
		super(action);
	}

	public Object handleExcel(Long entityId, Object value) {
		return handleWeb(entityId, value);
	}

	@Override
	public String handleWeb(Long entityId, Object value) {
		User assignedUser = translateValueToUser(value); 
		
		return renderUser(assignedUser);
	}

	private User translateValueToUser(Object value) {
		User assignedUser = UNASSIGNED_USER;
		if (value instanceof User) {
			assignedUser = (User)value;
		}
		return assignedUser;
	}

	private String renderUser(User assignedUser) {
		if (assignedUser  == null) {
			return action.getText("label.unassigned");
		}
		
		return assignedUser.getUserLabel();
	}
}