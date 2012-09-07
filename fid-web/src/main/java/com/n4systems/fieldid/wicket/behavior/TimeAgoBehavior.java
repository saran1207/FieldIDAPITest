package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

public class TimeAgoBehavior extends Behavior {

    private IModel<Date> dateModel;

    public TimeAgoBehavior(IModel<Date> dateModel) {
        this.dateModel = dateModel;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
        component.add(new AttributeModifier("title", dateTimeFormatter.print(new DateTime(dateModel.getObject()))));
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/jquery.timeago.js");
        response.renderOnDomReadyJavaScript("jQuery.timeago.settings.allowFuture = true;");
        response.renderOnDomReadyJavaScript("jQuery('#" + component.getMarkupId() + "').timeago();");
    }

}
