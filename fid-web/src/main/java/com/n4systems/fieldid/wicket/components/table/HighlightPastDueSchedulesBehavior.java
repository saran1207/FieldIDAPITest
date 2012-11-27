package com.n4systems.fieldid.wicket.components.table;

import com.n4systems.model.Event;
import com.n4systems.services.date.DateService;
import com.n4systems.util.views.RowView;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HighlightPastDueSchedulesBehavior extends AttributeAppender {

    private @SpringBean
    DateService dateService;

    private IModel<?> rowModel;

    public HighlightPastDueSchedulesBehavior(IModel<?> rowModel) {
        super("class", new Model<String>("pastDue"), " ");
        this.rowModel = rowModel;
    }

    @Override
    public boolean isEnabled(Component component) {
        Object object = rowModel.getObject();
        if (!(object instanceof RowView)) {
            return false;
        }
        Event event = (Event) ((RowView) object).getEntity();
        return event.getEventState() == Event.EventState.OPEN && dateService.isPastDue(event.getDueDate());
    }

}
