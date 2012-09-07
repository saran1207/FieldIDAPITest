package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.TimeAgoBehavior;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.util.Date;

public class TimeAgoLabel extends Label {

    public TimeAgoLabel(String id, IModel<Date> dateModel) {
        super(id, new DayDisplayModel(dateModel).includeTime());
        add(new TimeAgoBehavior(dateModel));
    }

}
