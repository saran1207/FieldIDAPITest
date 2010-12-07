package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.Status;

public class WsStatusTest {
	
	@Test
	public void maps_status_types_correctly() {
		assertEquals(WsStatus.FAIL, WsStatus.convert(Status.FAIL));
		assertEquals(WsStatus.PASS, WsStatus.convert(Status.PASS));
		assertEquals(WsStatus.NA, WsStatus.convert(Status.NA));
	}
	
	@Test
	public void maps_all_status_types() {
		// if there are any unmapped status types, WsStatus.convert will throw
		for (Status status: Status.values()) {
			WsStatus.convert(status);
		}
	}
	
}
