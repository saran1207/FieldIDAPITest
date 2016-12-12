package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.event.ActionWithCriteriaResults;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.model.CriteriaType;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.util.List;

public class NumberCriteriaValidator extends FieldValidatorSupport {

	@Override
	public void validate(Object action) throws ValidationException {
        List<CriteriaResultWebModel> criteriaResults = ((ActionWithCriteriaResults) action).getCriteriaResults();
        if (criteriaResults != null) {
            verifyAllNumberfieldCriteria(action, criteriaResults);
        }
    }

    private void verifyAllNumberfieldCriteria(Object action, List<CriteriaResultWebModel> criteriaResults) {
        for (CriteriaResultWebModel criteriaResult : criteriaResults) {
            if (CriteriaType.NUMBER_FIELD.name().equals(criteriaResult.getType())) {
            	if(criteriaResult.getTextValue() != null && !criteriaResult.getTextValue().isEmpty()) {
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

}
