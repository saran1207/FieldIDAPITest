package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.event.criteria.factory.CriteriaResultFactory;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class CriteriaSectionViewPanel extends Panel {
    public CriteriaSectionViewPanel(String id, final Class<? extends Event> eventClass, IModel<List<CriteriaResult>> results) {
        super(id);

        add(new ListView<CriteriaResult>("criteria", results) {

            @Override
            protected void populateItem(ListItem<CriteriaResult> item) {
                item.add(new Label("criteriaName", new PropertyModel<String>(item.getModel(), "criteria.displayText")));
                item.add(CriteriaResultFactory.createResultPanelFor("criteriaResult", item.getModel()));
            }
        });
    }
}
