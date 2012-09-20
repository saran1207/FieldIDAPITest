package com.n4systems.fieldid.wicket.behavior;

import com.n4systems.util.DateHelper;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.TimeZone;

public class TimeAgoBehavior extends Behavior {

    private IModel<Date> dateModel;

    private TimeZone timeZone;

    public TimeAgoBehavior(IModel<Date> dateModel) {
        this.dateModel = dateModel;
    }

    public TimeAgoBehavior withTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();

        Date date = new Date(dateModel.getObject().getTime());
        DateTime dateTime = null;
        if (timeZone!=null) {
            dateTime = new DateTime(DateHelper.convertToUTC(date, DateTimeZone.forOffsetHours(-4).toTimeZone()));
        } else {
            dateTime = new DateTime(date);
        }
        component.add(new AttributeModifier("title", dateTimeFormatter.print(dateTime)));
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/jquery.timeago.js");
        response.renderOnDomReadyJavaScript("jQuery.timeago.settings.allowFuture = true;");
        response.renderOnDomReadyJavaScript("jQuery('#" + component.getMarkupId() + "').timeago();");
    }

}
