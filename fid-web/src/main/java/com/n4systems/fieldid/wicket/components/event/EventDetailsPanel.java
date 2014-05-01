package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventDetailsPanel extends Panel {

    public EventDetailsPanel(String id, final IModel<? extends Event> model) {
        super(id, model);

        TimeZone timeZone = FieldIDSession.get().getSessionUser().getTimeZone();
        add(new Label("title", new FIDLabelModel("label.x_information", model.getObject().getType().getDisplayName())));
        add(new Label("performedBy", new PropertyModel(model, "performedBy.fullName")));
        add(new Label("datePerformed", new DayDisplayModel(new PropertyModel<Date>(model, "completedDate"), true, timeZone)));
        add(new Label("assignedTo", new PropertyModel(model, "assignedTo.assignedUser.userLabel")).setVisible(model.getObject().getType().isAssignedToAvailable()));

        add(new Label("scheduledOn", new DayDisplayModel(new PropertyModel<Date>(model, "dueDate"), true, timeZone)));
        add(new Label("eventbook", new PropertyModel(model, "book.name")));

        add(new ListView<String>("eventAttribute", new PropertyModel<List<? extends String>>(model, "type.infoFieldNames")) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("label", item.getModel()));
                item.add(new Label("value", new PropertyModel<String>(model, "infoOptionMap[" + item.getModelObject() + "]")));
            }
        });
    }
}
