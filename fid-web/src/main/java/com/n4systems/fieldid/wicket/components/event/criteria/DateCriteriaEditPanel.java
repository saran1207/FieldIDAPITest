package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.DateFieldCriteriaResult;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

public class DateCriteriaEditPanel extends Panel {

    public DateCriteriaEditPanel(String id, IModel<DateFieldCriteriaResult> result) {
        super(id);
        
        DateFieldCriteria dateField = (DateFieldCriteria)result.getObject().getCriteria();

        IModel<Date> dateModel = new UserToUTCDateModel(new PropertyModel<Date>(result, "value"));
        DateTimePicker dateTimePicker = new DateTimePicker("dateField", dateModel, dateField.isIncludeTime());

        dateTimePicker.withNoAllDayCheckbox().withoutPerformSetDateOnInitialization();
        
        dateTimePicker.addToDateField(new UpdateComponentOnChange());
        add(dateTimePicker);
    }

}
