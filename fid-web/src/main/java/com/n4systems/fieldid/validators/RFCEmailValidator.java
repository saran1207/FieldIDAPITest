package com.n4systems.fieldid.validators;

import com.n4systems.api.validation.validators.EmailValidator;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class RFCEmailValidator extends FieldValidatorSupport {
    @Override
    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        String value = null;
        try {
            value = (String)this.getFieldValue(fieldName, object);
        } catch ( ClassCastException e ) {
            // do nothing pass the null to the validator.  this way Longs can also use this validator.
        }

        if(!EmailValidator.EMAIL_REGEX.matcher(value).matches()) {
            addFieldError(fieldName, object);
            return;
        }
    }
}
