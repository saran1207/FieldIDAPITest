package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateLabel extends Label {

    private IModel<Date> model;
    private boolean allowTime = false;

    public DateLabel(String id, IModel<Date> model) {
        super(id, model);
        this.model = model;
    }

    public DateLabel withTimeAllowed() {
        allowTime = true;
        return this;
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        Date date = model.getObject();
        if (date==null) {
            return;
        }
        DateFormat df = !allowTime || new LocalDate(date).toDate().equals(date) ?
                new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateFormat()) :
                new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateTimeFormat());
        String dateString = df.format(date);
        replaceComponentTagBody(markupStream, openTag, dateString);
    }

}
