package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Status;

@XmlRootElement
public enum WsStatus {
	PASS, FAIL, NA;
	
	public static WsStatus convert(Status status) {
		switch (status) {
			case PASS:
				return PASS;
			case FAIL:
				return FAIL;
			case NA:
				return NA;
			default:
				throw new InvalidArgumentException("Unhandled Status: " + status.name());
		}
	}
}
