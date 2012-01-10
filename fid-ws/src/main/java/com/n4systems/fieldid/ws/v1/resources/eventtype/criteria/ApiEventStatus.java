package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Status;

public enum ApiEventStatus {
	PASS, FAIL, NA;
	
	public static ApiEventStatus convert(Status status) {
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
