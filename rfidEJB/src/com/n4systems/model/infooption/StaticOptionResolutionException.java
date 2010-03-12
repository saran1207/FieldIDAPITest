package com.n4systems.model.infooption;

import rfid.ejb.entity.InfoFieldBean;

@SuppressWarnings("serial")
public class StaticOptionResolutionException extends InfoOptionConversionException {
	private final String infoOptionName;
	
	public StaticOptionResolutionException(InfoFieldBean infoField, String infoOptionName) {
		super(String.format("Missing InfoOption [%s] for InfoField %s", infoOptionName, infoField), infoField);
		this.infoOptionName = infoOptionName;
	}

	public String getInfoOptionName() {
		return infoOptionName;
	}

}
