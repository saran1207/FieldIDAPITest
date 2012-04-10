package com.n4systems.api.validation.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.*;
import com.n4systems.model.builders.*;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.api.conversion.event.CriteriaResultViewBuilder;
import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.api.validation.ValidationResult;

public class CriteriaValidatorTest {
    private String SECTION_TITLE = "aSection";
    private String COMBO_TITLE = "criteriaA";
    private String ONE_CLICK_TITLE = "criteriaB";
    private String DATE_TITLE = "date";
    private String TEXT_FIELD_TITLE = "text_field";
    private String SELECT_TITLE = "select";
    private String UNIT_OF_MEASURE_TITLE = "unitOfMeasure";
    private String NUMBER_TITLE = "number";

	@Test
	public void validation_happy_path() {
		
		List<CriteriaResultView> values = Lists.newArrayList(
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(COMBO_TITLE).build(),
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(ONE_CLICK_TITLE).build(),			
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(DATE_TITLE).withResult(new Date()).build(),
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(TEXT_FIELD_TITLE).withSection(SECTION_TITLE).build(),
                new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(SELECT_TITLE).withResult("hello").build(),
                new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(NUMBER_TITLE).withResult("99").build(),
				new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(UNIT_OF_MEASURE_TITLE).withResult("feet|inches").withSection(SECTION_TITLE).build()
		);
				
		CriteriaResultValidator validator = new CriteriaResultValidator();
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
	
		ComboBoxCriteria comboBoxCriteria = ComboBoxCriteriaBuilder.aComboBoxCriteria().withDisplayText(COMBO_TITLE).build();
		OneClickCriteria oneClickCriteria = OneClickCriteriaBuilder.aCriteria().withStateSet(StateSetBuilder.aStateSet().build()).withDisplayText(ONE_CLICK_TITLE).build();
		DateFieldCriteria dateFieldCriteria = new DateFieldCriteriaBuilder(DATE_TITLE).build();
		SelectCriteria selectCriteria = new SelectCriteriaBuilder(SELECT_TITLE).withOptions("hello", "goodbye"). build();
		TextFieldCriteria textFieldCriteria = new TextFieldCriteriaBuilder(TEXT_FIELD_TITLE).build();
        NumberFieldCriteria numberCriteria = new NumberFieldCriteriaBuilder().withDisplayText(NUMBER_TITLE).build();
		UnitOfMeasureCriteria unitOfMeasureCriteria = new UnitOfMeasureCriteriaBuilder(UNIT_OF_MEASURE_TITLE).build();
		
		CriteriaSection[] sections = {
				CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
					withCriteria(comboBoxCriteria, oneClickCriteria, dateFieldCriteria, selectCriteria, textFieldCriteria, numberCriteria, unitOfMeasureCriteria).build(),
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
    public void validation_blank_select_fails() {

        List<CriteriaResultView> values = Lists.newArrayList(
                new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(SELECT_TITLE).withResult(null/*this should pass*/).build()
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

        assertTrue(result.isPassed());
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
    
    @Test
    public void validation_blank_number() {
        List<CriteriaResultView> values = Lists.newArrayList(
                new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(NUMBER_TITLE).withResult("").build()
        );

        CriteriaResultValidator validator = new CriteriaResultValidator();

        Map<String, Object> validationContext = new HashMap<String, Object>();

        NumberFieldCriteria numberCriteria = new NumberFieldCriteriaBuilder().withDisplayText(NUMBER_TITLE).build();

        CriteriaSection[] sections = {
                CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
                        withCriteria(numberCriteria).build(),
        };
        EventForm eventForm = EventFormBuilder.anEventForm().
                withSections(sections).build();

        EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();
        validationContext.put(EventViewValidator.EVENT_TYPE_KEY, eventType);

        ValidationResult result = validator.validate(values, null, "Field Name", null, null, validationContext);

        assertTrue(result.isPassed());

    }

    @Test
    public void validation_invalid_number() {
        List<CriteriaResultView> values = Lists.newArrayList(
                new CriteriaResultViewBuilder().aCriteriaResultView().withDisplayText(NUMBER_TITLE).withResult("this is an invalid number").build()
        );

        CriteriaResultValidator validator = new CriteriaResultValidator();

        Map<String, Object> validationContext = new HashMap<String, Object>();

        NumberFieldCriteria numberCriteria = new NumberFieldCriteriaBuilder().withDisplayText(NUMBER_TITLE).build();

        CriteriaSection[] sections = {
                CriteriaSectionBuilder.aCriteriaSection().withTitle(SECTION_TITLE).
                        withCriteria(numberCriteria).build(),
        };
        EventForm eventForm = EventFormBuilder.anEventForm().
                withSections(sections).build();

        EventType eventType = EventTypeBuilder.anEventType().withEventForm(eventForm).build();
        validationContext.put(EventViewValidator.EVENT_TYPE_KEY, eventType);

        ValidationResult result = validator.validate(values, null, "Field Name", null, null, validationContext);

        assertFalse(result.isPassed());
    }




}
