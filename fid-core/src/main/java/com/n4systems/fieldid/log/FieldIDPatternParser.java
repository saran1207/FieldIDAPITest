package com.n4systems.fieldid.log;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.model.user.User;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

public class FieldIDPatternParser extends PatternParser {

    private static final char TENANT_CHAR = 'T';
    private static final char USER_CHAR = 'U';

    public FieldIDPatternParser(String pattern) {
        super(pattern);
    }

    @Override
    protected void finalizeConverter(char formatChar) {
        PatternConverter pc = null;
        switch (formatChar) {
            case TENANT_CHAR:
                pc = new TenantFormatConverter();
                currentLiteral.setLength(0);
                addConverter(pc);
                break;
            case USER_CHAR:
                pc = new UserFormatConverter();
                currentLiteral.setLength(0);
                addConverter(pc);
                break;
            default:
                super.finalizeConverter(formatChar);
        }
    }

    static class UserFormatConverter extends PatternConverter {
        @Override
        protected String convert(LoggingEvent event) {
            User currentUser = ThreadLocalUserContext.getInstance().getCurrentUser();
            return currentUser == null ? "unknown user" : currentUser.getUserID();
        }
    }

    static class TenantFormatConverter extends PatternConverter {
        @Override
        protected String convert(LoggingEvent event) {
            User currentUser = ThreadLocalUserContext.getInstance().getCurrentUser();
            return currentUser == null ? "unknown tenant" : currentUser.getOwner().getTenant().getName();
        }
    }

}
