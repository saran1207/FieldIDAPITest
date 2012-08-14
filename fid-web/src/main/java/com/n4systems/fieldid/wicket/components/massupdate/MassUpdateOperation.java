package com.n4systems.fieldid.wicket.components.massupdate;

public enum MassUpdateOperation {
	EDIT   ("label.edit", "message.select_edit"),
    ASSIGN ("label.assign", "message.select_assign"),
    CLOSE  ("label.close", "message.select_close"),
	DELETE ("label.delete", "message.select_delete");
	
	private final String label;
	private final String message;
	
	MassUpdateOperation(String label, String message) {
		this.label = label;
		this.message = message;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getMessage() {
		return message;
	}
}