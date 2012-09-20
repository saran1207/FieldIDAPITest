package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.TimeAgoBehavior;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.util.Date;
import java.util.TimeZone;

public class TimeAgoLabel extends Label {

    public TimeAgoLabel(String id, IModel<Date> dateModel, TimeZone timeZone) {
        super(id, new DayDisplayModel(dateModel).includeTime());
        add(new TimeAgoBehavior(dateModel).withTimeZone(timeZone));
    }

}
