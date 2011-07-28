package com.n4systems.fieldid.wicket.components.renderer;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatChoiceRenderer implements IChoiceRenderer<String> {

    static final Date SAMPLE_DATE;

    static {
        try {
            SAMPLE_DATE = new SimpleDateFormat("MM/dd/yyyy").parse("12/31/1999");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getDisplayValue(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return format + " (" + sdf.format(SAMPLE_DATE) + ")";
    }

    @Override
    public String getIdValue(String object, int index) {
        return object;
    }

}
