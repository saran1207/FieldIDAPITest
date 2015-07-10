package com.n4systems.fieldid.ws.v2.exceptions;

public class NotFoundException extends javax.ws.rs.NotFoundException {

	public NotFoundException(String type, Object id) {
		super(type + "[" + id + "] Not Found");
	}


}
