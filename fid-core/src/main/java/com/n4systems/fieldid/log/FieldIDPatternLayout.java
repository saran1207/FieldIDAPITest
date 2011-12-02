package com.n4systems.fieldid.log;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

public class FieldIDPatternLayout extends PatternLayout {

    @Override
    protected PatternParser createPatternParser(String pattern) {
        return new FieldIDPatternParser(pattern);
    }

}
