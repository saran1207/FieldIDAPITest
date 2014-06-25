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
        if (dayModel.getObject() != null) {
            Date date = new Date(dayModel.getObject().getTime());
            //If the timezone isn't null AND the date doesn't contain a time of midnight...
            boolean convertTimeZone = timeZone != null && !DateUtil.isMidnight(date);
            boolean showTime = !DateUtil.isMidnight(date) && includeTime;
            return new FieldIdDateFormatter(date, FieldIDSession.get().getSessionUser(), convertTimeZone, showTime).format();
        } else
            return "";
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
