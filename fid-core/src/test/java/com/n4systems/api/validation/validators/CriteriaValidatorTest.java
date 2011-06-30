package com.n4systems.api.validation.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.api.conversion.event.CriteriaResultViewBuilder;
import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.SignatureCriteria;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.builders.ComboBoxCriteriaBuilder;
import com.n4systems.model.builders.CriteriaSectionBuilder;
import com.n4systems.model.builders.DateFieldCriteriaBuilder;
import com.n4systems.model.builders.EventFormBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.OneClickCriteriaBuilder;
import com.n4systems.model.builders.SelectCriteriaBuilder;
import com.n4systems.model.builders.StateSetBuilder;
import com.n4systems.model.builders.TextFieldCriteriaBuilder;
import com.n4systems.model.builders.UnitOfMeasureCriteriaBuilder;

public class CriteriaValidatorTest {
	String SECTION_TITLE = "aSection";
	String COMBO_TITLE = "criteriaA";
	String ONE_CLICK_TITLE = "criteriaB";
	String DATE_TITLE = "date";
	String TEXT_FIELD_TITLE = "text_field";
	String SELECT_TITLE = "select";
	String UNIT_OF_MEASURE_TITLE = "unitOfMeasure";

	@Test
	public void validation_happy_path() {
		
		List<CriteriaResultView> values = Lists.newArrayList(
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(COMBO_TITLE).build(),
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(ONE_CLICK_TITLE).build(),			
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(DATE_TITLE).withResult(new Date()).build(),
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(TEXT_FIELD_TITLE).withSection(SECTION_TITLE).build(),				
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(SELECT_TITLE).withResult("hello").build(),
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(UNIT_OF_MEASURE_TITLE).withResult("feet|inches").withSection(SECTION_TITLE).build()				
		);
				
		CriteriaResultValidator validator = new CriteriaResultValidator();
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
	
		ComboBoxCriteria comboBoxCriteria = ComboBoxCriteriaBuilder.aComboBoxCriteria().withDisplayText(COMBO_TITLE).build();
		OneClickCriteria oneClickCriteria = OneClickCriteriaBuilder.aCriteria().withStateSet(StateSetBuilder.aStateSet().build()).withDisplayText(ONE_CLICK_TITLE).build();
		DateFieldCriteria dateFieldCriteria = new DateFieldCriteriaBuilder(DATE_TITLE).build();
		SelectCriteria selectCriteria = new SelectCriteriaBuilder(SELECT_TITLE).withOptions("hello", "goodbye"). build();
		TextFieldCriteria textFieldCriteria = new TextFieldCriteriaBuilder(TEXT_FIELD_TITLE).build();
		UnitOfMeasureCriteria unitOfMeasureCriteria = new UnitOfMeasureCriteriaBuilder(UNIT_OF_MEASURE_TITLE).build();
		
		CriteriaSection[] sections = {
				CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
					withCriteria(comboBoxCriteria, oneClickCriteria, dateFieldCriteria, selectCriteria, textFieldCriteria, unitOfMeasureCriteria).build(),
		};
		EventForm eventForm = EventFormBuilder.anEventForm().
								withSections(sections).build();
								
		EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();		
		validationContext.put(EventViewValidator.EVENT_TYPE_KEY, eventType);
		
		ValidationResult result = validator.validate(values, null, "Field Name", null, null, validationContext);
		
		assertTrue(result.isPassed());		
	}	
	
	@Test
	public void validation_signature_fails_because_not_supported() {
		
		List<CriteriaResultView> values = Lists.newArrayList(
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(COMBO_TITLE).withResult("hello").withSection(SECTION_TITLE).build()
		);
				
		CriteriaResultValidator validator = new CriteriaResultValidator();
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		
		SignatureCriteria signatureCriteria = new SignatureCriteria();
		signatureCriteria.setDisplayText(COMBO_TITLE);
		
		CriteriaSection[] sections = {
				CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
					withCriteria(signatureCriteria).build(),
		};
		EventForm eventForm = EventFormBuilder.anEventForm().
								withSections(sections).build();
								
		EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();		
		validationContext.put(EventViewValidator.EVENT_TYPE_KEY, eventType);
		
		ValidationResult result = validator.validate(values, null, "Field Name", null, null, validationContext);
		
		assertTrue(result.isFailed());
		assertEquals(FieldValidator.CriteriaValidatorSignatureFail, result.getMessage());
	}


	
	@Test
	public void validation_unit_of_measure_fails() {
		
		String unitOfMeasureValue = "yards|feet|inches|";
		List<CriteriaResultView> values = Lists.newArrayList(
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(COMBO_TITLE).withResult(unitOfMeasureValue).withSection(SECTION_TITLE).build()
		);
				
		CriteriaResultValidator validator = new CriteriaResultValidator();
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		
		UnitOfMeasureCriteria unitOfMeasureCriteria = new UnitOfMeasureCriteriaBuilder(COMBO_TITLE).build();
		CriteriaSection[] sections = {
				CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
					withCriteria(unitOfMeasureCriteria).build(),
		};
		EventForm eventForm = EventFormBuilder.anEventForm().
								withSections(sections).build();
								
		EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();		
		validationContext.put(EventViewValidator.EVENT_TYPE_KEY, eventType);
		
		ValidationResult result = validator.validate(values, null, "Field Name", null, null, validationContext);
		
		assertTrue(result.isFailed());
		assertEquals(String.format(FieldValidator.CriteriaValidatorUnitOfMeasureFail, unitOfMeasureValue), result.getMessage());
	}
		
	
	@Test
	public void validation_select_fails() {
		
		String value = "notInListOfOptions";
		List<CriteriaResultView> values = Lists.newArrayList(
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(SELECT_TITLE).withResult(value).build()
		);
				
		CriteriaResultValidator validator = new CriteriaResultValidator();
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
	
		SelectCriteria selectCriteria = new SelectCriteriaBuilder(SELECT_TITLE).withOptions("hello", "goodbye"). build();
		
		CriteriaSection[] sections = {
				CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
					withCriteria(selectCriteria).build(),
		};
		EventForm eventForm = EventFormBuilder.anEventForm().
								withSections(sections).build();
								
		EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();		
		validationContext.put(EventViewValidator.EVENT_TYPE_KEY, eventType);
		
		ValidationResult result = validator.validate(values, null, "Field Name", null, null, validationContext);
		
		assertTrue(result.isFailed());		
		assertEquals(String.format(FieldValidator.CriteriaValidatorSelectFail, value, selectCriteria.getDisplayName(), selectCriteria.getOptions()), result.getMessage());
	}	
	
	
	@Test
	public void validation_date_fails() {
		
		String invalidDateResult = "this should be a Date() object!";
		List<CriteriaResultView> values = Lists.newArrayList(
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(DATE_TITLE).withResult(invalidDateResult).build()
		);
				
		CriteriaResultValidator validator = new CriteriaResultValidator();
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
	
		DateFieldCriteria dateFieldCriteria = new DateFieldCriteriaBuilder(DATE_TITLE).build();
		
		CriteriaSection[] sections = {
				CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
					withCriteria(dateFieldCriteria).build(),
		};
		EventForm eventForm = EventFormBuilder.anEventForm().withSections(sections).build();
								
		EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();		
		validationContext.put(EventViewValidator.EVENT_TYPE_KEY, eventType);
		
		ValidationResult result = validator.validate(values, null, "Field Name", null, null, validationContext);
		
		assertTrue(result.isFailed());
		assertEquals(String.format(FieldValidator.CriteriaValidatorDateFail, invalidDateResult, dateFieldCriteria.getDisplayName()), result.getMessage());	
	}	
	
	
	@Test(expected = IllegalStateException.class)
	public void validation_rquireds_context() {
		
		List<CriteriaResultView> values = Lists.newArrayList(
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(DATE_TITLE).build()
		);
				
		CriteriaResultValidator validator = new CriteriaResultValidator();		
		
		ValidationResult result = validator.validate(values, null, "Field Name", null, null, null /*this can't be null..required by validator*/);
	}	
		
	
}
