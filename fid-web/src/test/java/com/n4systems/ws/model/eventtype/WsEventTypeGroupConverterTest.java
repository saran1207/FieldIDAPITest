package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.EventTypeGroup;

public class WsEventTypeGroupConverterTest {
	
	@Test
	public void from_model_converts_all_fields() {
		EventTypeGroup model = new EventTypeGroup();
		model.setId(10L);
		model.setName("name");
		
		WsEventTypeGroup wsModel = new WsEventTypeGroupConverter().fromModel(model);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getName(), wsModel.getName());
	}
	
}
