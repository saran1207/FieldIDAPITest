package com.n4systems.ws.model.eventtype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Score;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.SignatureCriteria;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.builders.UnitOfMeasureBuilder;
import com.n4systems.model.builders.UnitOfMeasureCriteriaBuilder;
import com.n4systems.ws.model.WsModelConverter;

public class WsCriteriaConverterTest {
	
	private void populateBaseCriteriaFields(Criteria model) {
		model.setId(10L);
		model.setDisplayText("display text");
		model.setRecommendations(Arrays.asList("rec1", "rec2"));
		model.setDeficiencies(Arrays.asList("def1", "def2"));
	}
	
	private void verifyBaseCriteriaFields(Criteria model, WsCriteria wsModel) {
		assertEquals((long)model.getId(), wsModel.getId());
		assertEquals(model.getDisplayText(), wsModel.getDisplayText());
		assertEquals(model.getRecommendations(), wsModel.getRecommendations());
		assertEquals(model.getDeficiencies(), wsModel.getDeficiencies());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields_on_one_click_criteria() {
		OneClickCriteria model = new OneClickCriteria();
		populateBaseCriteriaFields(model);

		model.setPrincipal(true);
		model.setStates(new StateSet());
		model.getStates().getStates().add(new State());
		
		List<WsState> wsStates = Arrays.asList(new WsState());
		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.expect(stateConverter.fromModels(model.getStates().getStates())).andReturn(wsStates);
		EasyMock.replay(stateConverter);
		
		WsModelConverter<Score, WsScore> scoreConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(scoreConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter, scoreConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		verifyBaseCriteriaFields(model, wsModel);
		assertEquals(WsOneClickCriteria.class, wsModel.getClass());
        assertEquals(model.isPrincipal(), ((WsOneClickCriteria)wsModel).isPrincipal());
		assertSame(wsStates, ((WsOneClickCriteria)wsModel).getStates());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields_on_text_field_criteria() {
		TextFieldCriteria model = new TextFieldCriteria();
		populateBaseCriteriaFields(model);

		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(stateConverter);
		
		WsModelConverter<Score, WsScore> scoreConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(scoreConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter, scoreConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		verifyBaseCriteriaFields(model, wsModel);
		assertEquals(WsTextFieldCriteria.class, wsModel.getClass());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields_on_signature_criteria() {
		SignatureCriteria model = new SignatureCriteria();
		populateBaseCriteriaFields(model);

		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(stateConverter);
		
		WsModelConverter<Score, WsScore> scoreConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(scoreConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter, scoreConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		verifyBaseCriteriaFields(model, wsModel);
		assertEquals(WsSignatureCriteria.class, wsModel.getClass());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields_on_select_criteria() {
		SelectCriteria model = new SelectCriteria();
		model.getOptions().addAll(Arrays.asList("one", "two", "three"));
		populateBaseCriteriaFields(model);

		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(stateConverter);
		
		WsModelConverter<Score, WsScore> scoreConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(scoreConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter,scoreConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		verifyBaseCriteriaFields(model, wsModel);
		assertEquals(WsSelectCriteria.class, wsModel.getClass());
		assertEquals(model.getOptions(), ((WsSelectCriteria)wsModel).getOptions());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields_on_combobox_criteria() {
		ComboBoxCriteria model = new ComboBoxCriteria();
		model.getOptions().addAll(Arrays.asList("one", "two", "three"));
		populateBaseCriteriaFields(model);

		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(stateConverter);
		
		WsModelConverter<Score, WsScore> scoreConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(scoreConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter, scoreConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		verifyBaseCriteriaFields(model, wsModel);
		assertEquals(WsComboBoxCriteria.class, wsModel.getClass());
		assertEquals(model.getOptions(), ((WsComboBoxCriteria)wsModel).getOptions());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_all_fields_on_unit_of_measure_criteria() {
		UnitOfMeasureCriteria model = UnitOfMeasureCriteriaBuilder.aUnitOfMeasureCriteria()
			.primaryUnit(UnitOfMeasureBuilder.aUnitOfMeasure().build())
			.secondaryUnit(UnitOfMeasureBuilder.aUnitOfMeasure().build())
			.build();
		
		populateBaseCriteriaFields(model);

		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(stateConverter);
		
		WsModelConverter<Score, WsScore> scoreConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(scoreConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter, scoreConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		verifyBaseCriteriaFields(model, wsModel);
		assertEquals(WsUnitOfMeasureCriteria.class, wsModel.getClass());
		assertEquals((long)model.getPrimaryUnit().getId(), (long)((WsUnitOfMeasureCriteria)wsModel).getPrimaryUnitId());
		assertEquals((long)model.getSecondaryUnit().getId(), (long)((WsUnitOfMeasureCriteria)wsModel).getSecondaryUnitId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void from_model_converts_unit_of_measure_criteria_with_single_unit() {
		UnitOfMeasureCriteria model = UnitOfMeasureCriteriaBuilder.aUnitOfMeasureCriteria()
			.primaryUnit(UnitOfMeasureBuilder.aUnitOfMeasure().build())
			.build();
		
		populateBaseCriteriaFields(model);

		WsModelConverter<State, WsState> stateConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(stateConverter);
		
		WsModelConverter<Score, WsScore> scoreConverter = EasyMock.createMock(WsModelConverter.class);
		EasyMock.replay(scoreConverter);
		
		WsCriteria wsModel = new WsCriteriaConverter(stateConverter, scoreConverter).fromModel(model);
		
		EasyMock.verify(stateConverter);
		
		verifyBaseCriteriaFields(model, wsModel);
		assertEquals(WsUnitOfMeasureCriteria.class, wsModel.getClass());
		assertEquals((long)model.getPrimaryUnit().getId(), (long)((WsUnitOfMeasureCriteria)wsModel).getPrimaryUnitId());
		assertNull(((WsUnitOfMeasureCriteria)wsModel).getSecondaryUnitId());
	}
	
}
