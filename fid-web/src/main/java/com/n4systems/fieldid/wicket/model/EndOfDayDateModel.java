package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;

import java.util.Date;

// A Model that wraps another date model. When its object is set, it sets its time to
// the end of the day. This is useful for models representing the "high" end of a date range.
public class EndOfDayDateModel implements IModel<Date> {

    private IModel<Date> dateModel;

    public EndOfDayDateModel(IModel<Date> dateModel) {
        this.dateModel = dateModel;
    }

    @Override
    public Date getObject() {
        return dateModel.getObject();
    }

    @Override
    public void setObject(Date date) {
        dateModel.setObject(date == null ? null : DateHelper.getEndOfDay(date));
    }

    @Override
    public void detach() {
        dateModel.detach();
    }

}
