package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.event.EventCrud;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.fieldid.viewhelpers.EventHelper;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.util.List;

public class ObservationsProcessingValidator extends FieldValidatorSupport {

    // WEB-2376
    // We need a hook during validation to process the observations (which are submitted in web model format)
    // To clear out dummy blank observations values so we don't display the plus signs over every single
    // recommendation and deficiency.

    @Override
    public void validate(Object action) throws ValidationException {
        EventCrud eventCrud = (EventCrud) action;
        List<CriteriaResultWebModel> criteriaResults = eventCrud.getCriteriaResults();
        EventHelper eventHelper = eventCrud.getEventHelper();

        for (CriteriaResultWebModel criteriaResult : criteriaResults) {
            eventHelper.processObservations(criteriaResult);
        }
    }

}
