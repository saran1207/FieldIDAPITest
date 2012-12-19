package com.n4systems.ws.model.eventtype;

import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.EventResult;

@XmlRootElement
public enum WsStatus {
	PASS, FAIL, NA;
	
	public static WsStatus convert(EventResult eventResult) {
		switch (eventResult) {
			case PASS:
				return PASS;
			case FAIL:
				return FAIL;
			case NA:
				return NA;
			default:
				throw new InvalidArgumentException("Unhandled Status: " + eventResult.name());
		}
	}
}
