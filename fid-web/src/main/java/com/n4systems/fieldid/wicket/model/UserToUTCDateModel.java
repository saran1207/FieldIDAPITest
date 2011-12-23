package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;

import java.util.Date;

public class UserToUTCDateModel implements IModel<Date> {

    private IModel<Date> dateModel;

    public UserToUTCDateModel(IModel<Date> dateModel) {
        this.dateModel = dateModel;
    }

    @Override
    public Date getObject() {
        Date date = dateModel.getObject();
        return date == null ? null : DateHelper.convertToUserTimeZone(date, FieldIDSession.get().getSessionUser().getTimeZone());
    }

    @Override
    public void setObject(Date date) {
        dateModel.setObject(date == null ? null : DateHelper.convertToUTC(date, FieldIDSession.get().getSessionUser().getTimeZone()));
    }

    @Override
    public void detach() {
        dateModel.detach();
    }


}
