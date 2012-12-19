package com.n4systems.ws.model.eventtype;

import com.n4systems.model.EventResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WsStatusTest {
	
	@Test
	public void maps_status_types_correctly() {
		assertEquals(WsStatus.FAIL, WsStatus.convert(EventResult.FAIL));
		assertEquals(WsStatus.PASS, WsStatus.convert(EventResult.PASS));
		assertEquals(WsStatus.NA, WsStatus.convert(EventResult.NA));
	}
	
	@Test
	public void maps_all_status_types() {
		// if there are any unmapped status types, WsStatus.convert will throw
		for (EventResult eventResult : EventResult.getValidEventResults()) {
			WsStatus.convert(eventResult);
		}
	}
	
}
