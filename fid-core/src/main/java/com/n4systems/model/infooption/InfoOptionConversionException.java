package com.n4systems.model.infooption;

import rfid.ejb.entity.InfoFieldBean;

@SuppressWarnings("serial")
public class InfoOptionConversionException extends Exception {
	private final InfoFieldBean infoField;
	private String valueToConvert;
	
	public InfoOptionConversionException(String message, InfoFieldBean infoField, String valueToConvert) {
		super(message);
		this.infoField = infoField;
		this.valueToConvert = valueToConvert;
	}

	public InfoOptionConversionException(String message, InfoFieldBean infoField) {
		this(message, infoField, null);
	}
	
	public InfoFieldBean getInfoField() {
		return infoField;
	}
	
	public String getValueToConvert() {
		return valueToConvert;
	}
	
}
