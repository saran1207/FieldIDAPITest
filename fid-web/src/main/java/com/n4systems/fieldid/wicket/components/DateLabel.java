package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateLabel extends Label {

    private IModel<Date> model;

    public DateLabel(String id, IModel<Date> model) {
        super(id, model);
        this.model = model;
    }

    @Override
    protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        DateFormat df = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateFormat());
        String dateString = df.format(model.getObject());
        replaceComponentTagBody(markupStream, openTag, dateString);
    }

}
