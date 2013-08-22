package com.n4systems.fieldid.wicket.util;

import com.n4systems.persistence.localization2.LocalizedText;
import org.apache.wicket.util.convert.converter.AbstractConverter;

import java.util.Locale;

// TODO DD : need to put in properties file entry for when error occurs?
public class LocalizedTextConverter extends AbstractConverter<LocalizedText> {

    @Override
    protected Class<LocalizedText> getTargetType() {
        return LocalizedText.class;
    }

    @Override
    public LocalizedText convertToObject(String value, Locale locale) {
        return new LocalizedText(value);
    }
}
