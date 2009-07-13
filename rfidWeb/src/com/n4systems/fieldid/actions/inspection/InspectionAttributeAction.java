package com.n4systems.fieldid.actions.inspection;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;

public class InspectionAttributeAction extends AbstractAction {

	public InspectionAttributeAction(PersistenceManager persistenceManager) {
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
