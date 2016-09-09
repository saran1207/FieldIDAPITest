package com.n4systems.ws.model.eventtype;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.ws.model.WsModelConverter;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class WsCriteriaSectionConverterTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields() {
		CriteriaSection model = new CriteriaSection();
		model.setId(10L);
		model.setTitle("title");
		model.getCriteria().add(new OneClickCriteria());
		
		List<WsCriteria> wsCriteria = new ArrayList<WsCriteria>();
		wsCriteria.add(new WsOneClickCriteria());

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
