package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.eventtype.EventTypeFormVersion;

public class WsEventTypeFormVersionConverterTest {
	@Test
	public void from_model_converts_all_fields() {
		EventTypeFormVersion model = new EventTypeFormVersion(10, 100);
		WsEventTypeFormVersion wsModel = new WsEventTypeFormVersionConverter().fromModel(model);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getVersion(), wsModel.getVersion());
	}
}
