package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.EventResult;

public enum ApiEventStatus {
	PASS, FAIL, NA;
	
	public static ApiEventStatus convert(EventResult eventResult) {
		switch (eventResult) {
			case PASS:
				return PASS;
			case FAIL:
				return FAIL;
			case NA:
				return NA;
			default:
				throw new InvalidArgumentException("Unhandled EventResult: " + eventResult.name());
		}
	}
}
