package com.n4systems.api.validation.validators;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.n4systems.api.conversion.event.EventToModelConverter;
import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.SignatureCriteria;
import com.n4systems.model.StateSet;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.security.SecurityFilter;


public class CriteriaResultValidator extends CollectionValidator<CriteriaResultView, Collection<CriteriaResultView>> {

	@Override
	protected <V extends ExternalModelView> ValidationResult validateElement(
			CriteriaResultView value, V view, String fieldName,
			SecurityFilter filter, SerializableField field,
			Map<String, Object> validationContext) {
		
		if (validationContext==null || validationContext.get(EventViewValidator.EVENT_TYPE_KEY)==null) { 
			throw new IllegalStateException("you must put the EventType instance in the validation context");
		}
		EventType eventType = (EventType) validationContext.get(EventViewValidator.EVENT_TYPE_KEY);		
		CriteriaSection section = getSection(value.getSection(), eventType.getEventForm().getAvailableSections());
		Criteria criteria = getCriteria(value.getDisplayText(), section);
		return validateCriteria(section, criteria, value);
	}

	private ValidationResult validateCriteria(CriteriaSection section, Criteria criteria, CriteriaResultView value) {
		if (section==null || criteria==null || criteria.getCriteriaType()==null) { 
			return ValidationResult.fail(FieldValidator.CriteriaValidatorNoSectionCriteriaFail, value.getDisplayText());
		}
		switch (criteria.getCriteriaType()) {
		case COMBO_BOX:
			return validateComboBox((ComboBoxCriteria) criteria, section, value);
		case DATE_FIELD:
			return validateDateField((DateFieldCriteria) criteria, section, value);
		case ONE_CLICK:
			return validateOneClick((OneClickCriteria)criteria, section, value);
		case SELECT:
			return validateSelect((SelectCriteria) criteria, section, value);
		case SIGNATURE:
			return validateSignature((SignatureCriteria) criteria, section, value);
		case TEXT_FIELD:
			return validateTextField((TextFieldCriteria) criteria, section, value);
		case UNIT_OF_MEASURE:
			return validateUnitOfMeasure((UnitOfMeasureCriteria) criteria, section, value);
		}
		return ValidationResult.pass();
	}

	private ValidationResult validateUnitOfMeasure(UnitOfMeasureCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		// NOTE : we allow anything to pass here...pipe delimited text to separate the primary & secondary. 
		return StringUtils.countMatches(value.getResultString(), EventToModelConverter.UNIT_OF_MEASURE_SEPARATOR)<2  ? 
				ValidationResult.pass() : 
				ValidationResult.fail(CriteriaValidatorUnitOfMeasureFail, value.getResultString());
	}

	private ValidationResult validateTextField(TextFieldCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		return ValidationResult.pass();
	}

	private ValidationResult validateSignature(SignatureCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		return ValidationResult.fail(FieldValidator.CriteriaValidatorSignatureFail);
	}

	private ValidationResult validateSelect(SelectCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		List<String> options = criteria.getOptions();
		final String option = value.getResultString();
		int index = Iterators.indexOf(options.iterator(), new Predicate<String>() {
			@Override public boolean apply(String value) {
				return StringUtils.equalsIgnoreCase(value,option);
			}			
		});		
		return index < 0 ?  
			ValidationResult.fail(FieldValidator.CriteriaValidatorSelectFail, value.getResultString(), criteria.getDisplayName(), options) :
			ValidationResult.pass();		
	}

	private ValidationResult validateOneClick(OneClickCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		StateSet states = criteria.getStates();
		return states.getState(value.getResultString()) == null ?  
			ValidationResult.fail(FieldValidator.CriteriaOneClickFail, value.getResultString(),criteria.getDisplayName(),states.getAvailableStateStrings()) :
			ValidationResult.pass();		
	}

	private ValidationResult validateDateField(DateFieldCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		// TODO DD : should i allow null dates to pass? 
		return (value.getResult()==null || value.getResult() instanceof Date ) ? 
				ValidationResult.pass() : 
				ValidationResult.fail(FieldValidator.CriteriaValidatorDateFail, value.getResult(), criteria.getDisplayName());
	}

	private ValidationResult validateComboBox(ComboBoxCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		return ValidationResult.pass();
	}

	private Criteria getCriteria(String displayText, CriteriaSection section) {
		if (section==null) {
			return null;
		}
		for (Criteria criteria:section.getAvailableCriteria()) {
			if (StringUtils.equalsIgnoreCase(criteria.getDisplayText(), displayText)) {
				return criteria;
			}
		}
		return null;
	}

	private CriteriaSection getSection(String sectionName,	List<CriteriaSection> availableSections) {
		if (availableSections==null) { 
			return null;
		}
		for (CriteriaSection section:availableSections) { 
			if (StringUtils.equalsIgnoreCase(section.getDisplayName(),sectionName)) {
				return section;
			}
		}
		return null;
	}


}
