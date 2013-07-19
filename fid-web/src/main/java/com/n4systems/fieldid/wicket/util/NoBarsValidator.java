package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.validation.validator.PatternValidator;

public class NoBarsValidator extends PatternValidator {
    public NoBarsValidator() {
        super(".*\\|.*");
        setReverse(true);
    }
}
