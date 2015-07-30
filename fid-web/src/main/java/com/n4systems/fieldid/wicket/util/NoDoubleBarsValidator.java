package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * A Pattern Validator for fields which ensures that two pipes in a row (||) are not allowed.
 *
 * Created by Jordan Heath on 2015-07-29.
 */
public class NoDoubleBarsValidator extends PatternValidator {
    public NoDoubleBarsValidator() {
        super(".*\\|{2}.*");
        setReverse(true);
    }
}
