package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.TimeAgoBehavior;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.util.DateHelper;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.Date;
import java.util.TimeZone;

public class TimeAgoLabel extends Label {

    public TimeAgoLabel(String id, IModel<Date> dateModel, TimeZone timeZone) {
        super(id, new DayDisplayModel(dateModel).includeTime());
        addTitleAttribute(dateModel, timeZone);
    }

    protected DayDisplayModel getDayDisplayModel() {
        return (DayDisplayModel) getDefaultModel();
    }

    protected void addTitleAttribute(IModel<Date> dateModel, TimeZone timeZone) {
        // default is to let TimeAgo handle setting the "title".  suggested
        add(new TimeAgoBehavior(new AdjustedDateModel(dateModel, timeZone)));
    }

    public TimeAgoLabel(String id, IModel<Date> dateModel) {
        this(id, dateModel, null);
    }


    class AdjustedDateModel extends LoadableDetachableModel<Date> {

        private IModel<Date> model;
        private TimeZone timeZone;

        AdjustedDateModel(IModel<Date> model, TimeZone timeZone) {
            super();
            this.model = model;
            this.timeZone = timeZone;
        }

        @Override
        protected Date load() {
            Date date = model.getObject();
            if (timeZone!=null) {
                date = DateHelper.convertToUTC(new Date(date.getTime()), timeZone);
            }
            return date;
        }

        @Override
        public void detach() {
            model.detach();
        }
    }
}
