package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.event.EventCrud;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.model.CriteriaType;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.util.List;

public class AllScoresMustBeEnteredValidator extends FieldValidatorSupport {

    @Override
    public void validate(Object action) throws ValidationException {
        List<CriteriaResultWebModel> criteriaResults = ((EventCrud) action).getCriteriaResults();
        for (CriteriaResultWebModel criteriaResult : criteriaResults) {
            if (CriteriaType.SCORE.name().equals(criteriaResult.getType()) && criteriaResult.getStateId() == null) {
                addFieldError(getFieldName(), action);
                break;
            }
        }
    }

}
