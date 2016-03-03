package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
public class EventAttributeAction extends AbstractAction {

	public EventAttributeAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private static final long serialVersionUID = 1L;
	
	private Long fieldIndex;
	
	public String doAdd() {
		return SUCCESS;
	}

	public Long getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex( Long fieldIndex ) {
		this.fieldIndex = fieldIndex;
	}
	
	

}
