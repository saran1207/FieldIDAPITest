package com.n4systems.fieldid.validators;

import com.n4systems.api.validation.validators.EmailValidator;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.util.ArrayList;
import java.util.List;

public class RFCEmailValidator extends FieldValidatorSupport {
    @Override
    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        String val = null;
        Object value = null;
        List<String> values = new ArrayList<String>();
        try {
            value = this.getFieldValue(fieldName, object);

            if (value instanceof List) {
                values = (List<String>)value;
                validateMatch(values, fieldName, object);

            } else {
                val = (String)value;
                validateMatch(val, fieldName, object);
            }


        } catch ( ClassCastException e ) {
            // do nothing pass the null to the validator.  this way Longs can also use this validator.
        }


    }

    private void validateMatch(String value, String fieldName, Object object) {
        if((!EmailValidator.EMAIL_REGEX.matcher(value).matches() && !value.isEmpty())) {
            addFieldError(fieldName, object);
            return;
        }
    }

    private void validateMatch(List<String> values, String fieldName, Object object) {
        for (String value: values) {
            if((!EmailValidator.EMAIL_REGEX.matcher(value).matches() && !value.isEmpty())) {
                addFieldError(fieldName, object);
                return;
            }
        }
    }

}
