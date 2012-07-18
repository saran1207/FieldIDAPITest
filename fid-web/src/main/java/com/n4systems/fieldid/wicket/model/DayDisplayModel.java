package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayDisplayModel extends LoadableDetachableModel<String> {

    private IModel<Date> dayModel;

    private boolean includeTime;

    public DayDisplayModel(IModel<Date> dayModel) {
        this.dayModel = dayModel;
    }
    
    @Override
    protected String load() {
        Date date = dayModel.getObject();
        if(includeTime)
            return new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateTimeFormat()).format(date);
        else
            return new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateFormat()).format(date);
    }

    public DayDisplayModel includeTime() {
        includeTime = true;
        return this;
    }
}
