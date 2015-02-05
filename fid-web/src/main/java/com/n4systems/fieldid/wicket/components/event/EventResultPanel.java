package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.model.ObservationCount;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventResultPanel extends Panel {

    Label eventResult;

    public EventResultPanel(String id, IModel<? extends Event> model) {
        super(id, model);

        add(new Label("score", new PropertyModel(model, "score")));

        EventFormHelper eventFormHelper = new EventFormHelper();

        Double percentage = eventFormHelper.getEventFormScorePercentage(model.getObject());
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        add(new Label("percentage", numberFormat.format(percentage))
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

        Map<ObservationCount, Integer> observationTotals = eventFormHelper.getFormObservationTotals(model.getObject());
        int formObservationTotal = eventFormHelper.getObservationCountTotal(model.getObject());

        add(new ListView<ObservationCount>("observationResult", new PropertyModel<List<ObservationCount>> (model, "type.eventForm.observationCountGroup.observationCounts")) {
            @Override
            protected void populateItem(ListItem<ObservationCount> item) {
                item.add(new Label("name", new PropertyModel<>(item.getModel(), "name")));
                item.add(new Label("total", observationTotals.get(item.getModelObject()).toString()));

                double percentage = observationTotals.get(item.getModelObject()) * 1.0d / formObservationTotal;

                item.add(new FlatLabel("percentage", numberFormat.format(percentage))
                        .setVisible(item.getModelObject().isCounted() && model.getObject().getEventType().isDisplayObservationPercentage()));
            }
        });
    }
}
