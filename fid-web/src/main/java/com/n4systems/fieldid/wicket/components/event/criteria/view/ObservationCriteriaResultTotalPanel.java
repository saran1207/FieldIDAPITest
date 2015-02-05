package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.ObservationCount;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;
import java.util.Map;

public class ObservationCriteriaResultTotalPanel extends Panel {
    public ObservationCriteriaResultTotalPanel(String id, IModel<? extends AbstractEvent> event, Map<ObservationCount, Integer> sectionObservations) {
        super(id);

        add(new ListView<ObservationCount>("observationResult", new PropertyModel<List<ObservationCount>>(event, "type.eventForm.observationCountGroup.observationCounts")) {
            @Override
            protected void populateItem(ListItem<ObservationCount> item) {
                item.add(new Label("name", new PropertyModel<>(item.getModel(), "name")));
                item.add(new Label("total", sectionObservations.get(item.getModelObject()).toString()));
                item.add(new Label("percentage").setVisible(false));
            }
        });


    }
}
