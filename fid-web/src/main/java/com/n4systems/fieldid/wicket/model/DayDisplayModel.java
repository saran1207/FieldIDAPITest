package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.time.DateUtil;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.Date;
import java.util.TimeZone;

public class DayDisplayModel extends LoadableDetachableModel<String> {

    private IModel<Date> dayModel;

    private boolean includeTime;
    private TimeZone timeZone;

    public DayDisplayModel(IModel<Date> dayModel, boolean includeTime, TimeZone timeZone) {
        this.dayModel = dayModel;
        this.includeTime = includeTime;
        this.timeZone = timeZone;
    }

    public DayDisplayModel(IModel<Date> dayModel, boolean includeTime) {
        this(dayModel, includeTime, null);
    }

    public DayDisplayModel(IModel<Date> dayModel) {
        this(dayModel, false);
    }

    @Override
    protected String load() {
        Date date = dayModel.getObject();
        boolean convertTimeZone = timeZone != null;
        boolean showTime = !DateUtil.isMidnight(date) && includeTime;
        return new FieldIdDateFormatter(date, FieldIDSession.get().getSessionUser(), convertTimeZone, showTime).format();
    }

    public DayDisplayModel includeTime() {
        includeTime = true;
        return this;
    }

    public DayDisplayModel withTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

}
