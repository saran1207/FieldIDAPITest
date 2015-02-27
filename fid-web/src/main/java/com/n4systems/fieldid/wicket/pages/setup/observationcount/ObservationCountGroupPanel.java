package com.n4systems.fieldid.wicket.pages.setup.observationcount;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ObservationCountGroupPanel extends Panel {
    public ObservationCountGroupPanel(String id, IModel<ObservationCountGroup> groupsModel) {
        super(id, groupsModel);

        setOutputMarkupPlaceholderTag(true);

        add(new ListView<ObservationCount>("observationCount", new PropertyModel<List<ObservationCount>>(groupsModel, "observationCounts")) {

            @Override
            protected void populateItem(ListItem<ObservationCount> item) {
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
                if (item.getModelObject().isCounted())
                    item.add(new Label("subtitle", new FIDLabelModel("label.included_in_total")));
                else
                    item.add(new Label("subtitle", new FIDLabelModel("label.not_included_in_total")));
            }
        });
    }
}
