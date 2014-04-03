package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.text.NumberFormat;

public class EventResultPanel extends Panel {

    Label eventResult;

    public EventResultPanel(String id, IModel<? extends Event> model) {
        super(id, model);

        add(new Label("score", new PropertyModel(model, "score")));

        Double percentage = new EventFormHelper().getEventFormScorePercentage(model.getObject());
        add(new Label("percentage", NumberFormat.getPercentInstance().format(percentage))
                .setVisible(model.getObject().getType().isDisplayScorePercentage()));

        add(eventResult = new Label("result", new PropertyModel(model, "eventResult.displayName")));

        if (model.getObject().getEventResult().equals(EventResult.PASS)) {
            eventResult.add(new AttributeAppender("class", "pass-color"));
        } else if (model.getObject().getEventResult().equals(EventResult.FAIL)) {
            eventResult.add(new AttributeAppender("class", "fail-color"));
        } else {
            eventResult.add(new AttributeAppender("class", "na-color").setSeparator(" "));
        }

        add(new Label("eventStatus", new PropertyModel(model, "eventStatus.displayName")));
    }
}
