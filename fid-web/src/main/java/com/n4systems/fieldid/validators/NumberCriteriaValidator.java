package com.n4systems.fieldid.validators;

import java.util.List;

import com.n4systems.fieldid.actions.event.EventCrud;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.model.CriteriaType;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class NumberCriteriaValidator extends FieldValidatorSupport {

	@Override
	public void validate(Object action) throws ValidationException {
        List<CriteriaResultWebModel> criteriaResults = ((EventCrud) action).getCriteriaResults();
        for (CriteriaResultWebModel criteriaResult : criteriaResults) {
            if (CriteriaType.NUMBER_FIELD.name().equals(criteriaResult.getType())) {            	
            	try {
					Float.parseFloat(criteriaResult.getTextValue());
				} catch (NumberFormatException e) {
	                addFieldError(getFieldName(), action);
	                break;
				}
            }
        }

	}

}
