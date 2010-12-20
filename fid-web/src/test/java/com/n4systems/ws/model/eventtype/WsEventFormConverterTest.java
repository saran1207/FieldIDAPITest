package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.ws.model.WsModelConverter;

public class WsEventFormConverterTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields() {
		EventForm model = new EventForm();
		model.setId(10L);
		model.getSections().add(new CriteriaSection());
		
		List<WsCriteriaSection> wsCritSections = Arrays.asList(new WsCriteriaSection());
		WsModelConverter<CriteriaSection, WsCriteriaSection> sectionConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(sectionConverter.fromModels(model.getSections())).andReturn(wsCritSections);
		EasyMock.replay(sectionConverter);
		
		WsEventForm wsModel = new WsEventFormConverter(sectionConverter).fromModel(model);

		EasyMock.verify(sectionConverter);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertSame(wsCritSections, wsModel.getSections());
	}
}
