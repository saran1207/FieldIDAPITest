package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import com.n4systems.model.EventResult;
import org.junit.Test;

import com.n4systems.model.State;

public class WsStateConverterTest {

	@Test
	public void from_model_converts_all_fields() {
		State model = new State();
		model.setId(10L);
		model.setDisplayText("display text");
		model.setButtonName("button name");
		model.setEventResult(EventResult.FAIL);
		model.setRetired(false);
		
		WsState wsModel = new WsStateConverter().fromModel(model);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getDisplayText(), wsModel.getDisplayText());
		assertEquals(model.getButtonName(), wsModel.getButtonName());
		assertEquals(WsStatus.FAIL, wsModel.getStatus());
		assertTrue(wsModel.isActive());
	}
	
}
