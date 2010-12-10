package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.OneClickCriteria;
import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.Criteria;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.ws.model.WsModelConverter;

public class WsCriteriaConverterTest {
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields() {
		OneClickCriteria model = new OneClickCriteria();
		model.setId(10L);
		model.setDisplayText("display text");
		model.setPrincipal(true);
		model.setRecommendations(Arrays.asList("rec1", "rec2"));
		model.setDeficiencies(Arrays.asList("def1", "def2"));
		model.setStates(new StateSet());
		model.getStates().getStates().add(new State());
		
		List<WsState> wsStates = Arrays.asList(new WsState());
		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
//		EasyMock.expect(stateConverter.fromModels(model.getStates().getStates())).andReturn(wsStates);
		EasyMock.replay(stateConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getDisplayText(), wsModel.getDisplayText());
		assertEquals(model.isPrincipal(), wsModel.isPrincipal());
		assertEquals(model.getRecommendations(), wsModel.getRecommendations());
		assertEquals(model.getDeficiencies(), wsModel.getDeficiencies());
//		assertSame(wsStates, wsModel.getStates());
	}
}
