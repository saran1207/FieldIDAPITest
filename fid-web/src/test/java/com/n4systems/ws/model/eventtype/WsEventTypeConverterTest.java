package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.EventForm;
import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventTypeConverterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields() {
		EventType model = new EventType();
		model.setId(10L);
		model.setName("name");
		model.setDescription("description");
		model.setPrintable(true);
		model.setMaster(true);
		model.makeAssignedToAvailable();
		model.setGroup(new EventTypeGroup());
        model.setEventForm(new EventForm());
		model.getEventForm().getSections().add(new CriteriaSection());
		
		WsEventTypeGroup wsGroup = new WsEventTypeGroup();
		WsModelConverter<EventTypeGroup, WsEventTypeGroup> groupConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(groupConverter.fromModel(model.getGroup())).andReturn(wsGroup);
		EasyMock.replay(groupConverter);
		
		List<WsCriteriaSection> wsCritSections = Arrays.asList(new WsCriteriaSection());
		WsModelConverter<CriteriaSection, WsCriteriaSection> sectionConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(sectionConverter.fromModels(model.getEventForm().getSections())).andReturn(wsCritSections);
		EasyMock.replay(sectionConverter);
		
		WsEventType wsModel = new WsEventTypeConverter(sectionConverter, groupConverter).fromModel(model);
		
		EasyMock.verify(groupConverter);
		EasyMock.verify(sectionConverter);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getName(), wsModel.getName());
		assertEquals(model.getDescription(), wsModel.getDescription());
		assertEquals(model.isPrintable(), wsModel.isPrintable());
		assertEquals(model.isMaster(), wsModel.isMaster());
		assertEquals(model.isAssignedToAvailable(), wsModel.isAssignedToAvailable());
		assertSame(wsGroup, wsModel.getGroup());
		assertSame(wsCritSections, wsModel.getSections());
	}
	
}
