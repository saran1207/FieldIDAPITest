package com.n4systems.fieldid.wicket.behavior.validation;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class ConsiderAbsentIfEqualToValidator implements IValidator<String> {

    private IModel<String> descriptionText;

    public ConsiderAbsentIfEqualToValidator(IModel<String> descriptionText) {
        this.descriptionText = descriptionText;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        if (validatable.getValue() != null && descriptionText.getObject() != null && validatable.getValue().equals(descriptionText.getObject())) {
            ValidationError error = new ValidationError();
            error.addMessageKey("Required");

            validatable.error(error);
        }
    }

}
