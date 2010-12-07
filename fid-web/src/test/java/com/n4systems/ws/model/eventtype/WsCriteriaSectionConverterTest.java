package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.ws.model.WsModelConverter;

public class WsCriteriaSectionConverterTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields() {
		CriteriaSection model = new CriteriaSection();
		model.setId(10L);
		model.setTitle("title");
		model.getCriteria().add(new Criteria());
		
		List<WsCriteria> wsCriteria = Arrays.asList(new WsCriteria());
		WsModelConverter<Criteria, WsCriteria> criteriaConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(criteriaConverter.fromModels(model.getCriteria())).andReturn(wsCriteria);
		EasyMock.replay(criteriaConverter);
		
		WsCriteriaSection wsModel = new WsCriteriaSectionConverter(criteriaConverter).fromModel(model);
		
		EasyMock.verify(criteriaConverter);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getTitle(), wsModel.getTitle());
		assertSame(wsCriteria, wsModel.getCriteria());
	}
}
