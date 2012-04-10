package com.n4systems.api.validation.validators;

import java.util.ArrayList;
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
import com.n4systems.model.*;
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
			return ValidationResult.fail(FieldValidator.CriteriaValidatorNoSectionCriteriaFail, value.getSection(), value.getDisplayText());
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
		case NUMBER_FIELD:
			return validateNumberField((NumberFieldCriteria) criteria, section, value);
		case SCORE:
			return validateScoreField((ScoreCriteria)criteria, section, value);
		default :
			return ValidationResult.fail("unsupported critera " + criteria.getClass().getSimpleName() + " : most likely you have added a criteria type without adding validation");
		}
	}

	private ValidationResult validateScoreField(ScoreCriteria criteria, CriteriaSection section, CriteriaResultView value) {
		List<Score> scores = criteria.getScoreGroup().getScores();
		List<String> scoreNames = new ArrayList<String>();
		for (Score score:scores) { 
			if (score.getName().equals(value.getResultString())) {
				return ValidationResult.pass();
			}
			scoreNames.add(score.getName());
		}
		return ValidationResult.fail(InvalidScoreFail, value.getResultString(),scoreNames);
	}

	private ValidationResult validateNumberField(NumberFieldCriteria criteria, CriteriaSection section, CriteriaResultView value) {
        String result = value.getResultString();
        if (StringUtils.isNotBlank(result)) {   // note : blanks are acceptable
            try {
                Float.parseFloat(result);
            } catch (Exception e) {
                return ValidationResult.fail(NotANumberFail, result, criteria.getDisplayName());
            }
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

        // We need to allow for an empty value
        if (StringUtils.isBlank(value.getResultString()))
            return ValidationResult.pass();

		return states.getState(value.getResultString()) == null ?
			ValidationResult.fail(FieldValidator.CriteriaOneClickFail, value.getResultString(),criteria.getDisplayName(),states.getAvailableStateStrings()) :
			ValidationResult.pass();		
	}

	private ValidationResult validateDateField(DateFieldCriteria criteria, CriteriaSection section, CriteriaResultView value) {
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
