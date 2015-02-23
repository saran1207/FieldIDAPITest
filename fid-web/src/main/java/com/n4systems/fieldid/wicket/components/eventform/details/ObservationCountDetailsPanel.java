package com.n4systems.fieldid.wicket.components.eventform.details;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountCriteria;
import com.n4systems.model.TextFieldCriteria;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ObservationCountDetailsPanel extends Panel {

    public ObservationCountDetailsPanel(String id, IModel<ObservationCountCriteria> model) {
        super(id, model);

        add(new AttributeAppender("class", "scoreGroupDetails"));
        add(new ListView<ObservationCount>("observationCount", new PropertyModel<List<ObservationCount>>(model, "observationCountGroup.observationCounts")) {
            @Override
            protected void populateItem(ListItem<ObservationCount> item) {
                item.add(new FlatLabel("name", new PropertyModel<>(item.getModel(), "name")));
                item.add(new FlatLabel("counted", getIsCountedLabel(item.getModel())));
            }
        });
    }

    private IModel<String> getIsCountedLabel(IModel<ObservationCount> model) {
        if (model.getObject().isCounted())
            return new FIDLabelModel("label.included_in_total");
        else
            return new FIDLabelModel("label.not_included_in_total");
    }
}
