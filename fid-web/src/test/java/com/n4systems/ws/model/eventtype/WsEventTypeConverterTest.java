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
		
		WsEventTypeGroup wsGroup = new WsEventTypeGroup();
		WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(groupConverter.fromModel(model.getGroup())).andReturn(wsGroup);
		EasyMock.replay(groupConverter);

		WsEventForm wsEventForm = new WsEventForm();
		WsModelConverter<EventForm, WsEventForm> formConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(formConverter.fromModel(model.getEventForm())).andReturn(wsEventForm);
		EasyMock.replay(formConverter);
		
		WsEventType wsModel = new WsEventTypeConverter(formConverter, groupConverter).fromModel(model);
		
		EasyMock.verify(groupConverter);
		EasyMock.verify(formConverter);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getName(), wsModel.getName());
		assertEquals(model.getDescription(), wsModel.getDescription());
		assertEquals(model.isPrintable(), wsModel.isPrintable());
		assertEquals(model.isMaster(), wsModel.isMaster());
		assertEquals(model.isAssignedToAvailable(), wsModel.isAssignedToAvailable());
		assertArrayEquals(model.getInfoFieldNames().toArray(), wsModel.getInfoFieldNames().toArray());
		assertSame(wsGroup, wsModel.getGroup());
		assertSame(wsEventForm, wsModel.getForm());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void does_not_attempt_form_conversion_when_null() {
		EventType model = EventTypeBuilder.anEventType().withEventForm(null).build();
		
		WsModelConverter<EventForm, WsEventForm> formConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(formConverter);
		
		WsEventType wsModel = new WsEventTypeConverter(formConverter, EasyMock.createMock(WsModelConverter.class)).fromModel(model);
		EasyMock.verify(formConverter);
		
		assertNull(wsModel.getForm());
	}
	
}
