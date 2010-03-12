package com.n4systems.model.infooption;

import rfid.ejb.entity.InfoFieldBean;

@SuppressWarnings("serial")
public class MissingInfoOptionException extends InfoOptionConversionException {

	public MissingInfoOptionException(InfoFieldBean infoField) {
		super("Missing InfoOption for InfoField [" + infoField.getName() + "]", infoField);
	}
	
}
