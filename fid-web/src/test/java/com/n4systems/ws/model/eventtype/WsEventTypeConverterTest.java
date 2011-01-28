package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventTypeConverterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields() {
		EventType model = EventTypeBuilder.anEventType()
								.named("name")
								.withDescription("description")
								.withPrintable(true)
								.withMaster(true)
								.withAssignedToAvailable()
								.withGroup(new EventTypeGroup())
								.withEventForm(new EventForm())
								.withInfoFieldNames("info field")
								.withId(10L)
								.build();
		
		model.getEventForm().setId(2l);
		
		WsEventTypeGroup wsGroup = new WsEventTypeGroup();
		WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(groupConverter.fromModel(model.getGroup())).andReturn(wsGroup);
		EasyMock.replay(groupConverter);
		
		WsEventType wsModel = new WsEventTypeConverter(groupConverter).fromModel(model);
		
		EasyMock.verify(groupConverter);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getName(), wsModel.getName());
		assertEquals(model.getDescription(), wsModel.getDescription());
		assertEquals(model.isPrintable(), wsModel.isPrintable());
		assertEquals(model.isMaster(), wsModel.isMaster());
		assertEquals(model.isAssignedToAvailable(), wsModel.isAssignedToAvailable());
		assertSame(model.getEventForm().getId(), wsModel.getFormId());
		assertArrayEquals(model.getInfoFieldNames().toArray(), wsModel.getInfoFieldNames().toArray());
		assertSame(wsGroup, wsModel.getGroup());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void does_not_attempt_form_conversion_when_null() {
		EventType model = EventTypeBuilder.anEventType().withEventForm(null).build();
		WsEventType wsModel = new WsEventTypeConverter(EasyMock.createMock(WsModelConverter.class)).fromModel(model);
		
		assertNull(wsModel.getFormId());
	}
	
}
