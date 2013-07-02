package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import com.n4systems.util.StringUtils;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import rfid.ejb.entity.InfoFieldBean;

public class ValidateIfRequiredValidator<T> implements INullAcceptingValidator<T> {

    private InfoFieldBean infoField;

    public ValidateIfRequiredValidator(InfoFieldBean infoField) {
        this.infoField = infoField;
    }

    @Override
    public void validate(IValidatable<T> validatable) {
        T value = validatable.getValue();
        if (infoField.isRequired() && ( value == null || StringUtils.isEmpty(value.toString()) )) {
            validatable.error(new ValidationError().addMessageKey("RequiredAttribute").setVariable("attributeName", infoField.getName()));
        }
    }

}
