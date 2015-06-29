package com.n4systems.fieldid.api.mobile.exceptions;

public class NotFoundException extends javax.ws.rs.NotFoundException {

	public NotFoundException(String type, Object id) {
		super(type + "[" + id + "] Not Found");
	}


}
