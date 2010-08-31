package com.n4systems.model.infooption;

import rfid.ejb.entity.InfoFieldBean;

@SuppressWarnings("serial")
public class InfoOptionConversionException extends Exception {
	private final InfoFieldBean infoField;
	
	public InfoOptionConversionException(String message, InfoFieldBean infoField) {
		super(message);
		this.infoField = infoField;
	}

	public InfoFieldBean getInfoField() {
		return infoField;
	}
}
