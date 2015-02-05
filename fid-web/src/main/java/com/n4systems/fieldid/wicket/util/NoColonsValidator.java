package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.validation.validator.PatternValidator;

public class NoColonsValidator extends PatternValidator {
    public NoColonsValidator() {
        super(".*:.*");
        setReverse(true);
    }
}
