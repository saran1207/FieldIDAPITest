package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.criteria;

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
