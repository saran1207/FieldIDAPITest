package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.DateFieldCriteriaResult;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.TimeZone;

public class DateCriteriaResultPanel extends Panel{
    public DateCriteriaResultPanel(String id, IModel<DateFieldCriteriaResult> resultModel) {
        super(id);

        TimeZone timeZone = FieldIDSession.get().getSessionUser().getTimeZone();
        boolean includeTime = ((DateFieldCriteria) resultModel.getObject().getCriteria()).isIncludeTime();

        add(new Label("dateResult", new DayDisplayModel(new PropertyModel<Date>(resultModel, "value"), includeTime, timeZone)));
    }
}
