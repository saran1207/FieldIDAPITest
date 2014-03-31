package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class EventFormViewPanel extends EventFormPanel {


    public EventFormViewPanel(String id, Class<? extends AbstractEvent> eventClass, IModel<List<AbstractEvent.SectionResults>> results) {
        super(id, eventClass, results);
        setVisible(!results.getObject().isEmpty());
    }

    @Override
    protected Panel getCriteriaSectionPanel(Class<? extends AbstractEvent> eventClass, PropertyModel<List<CriteriaResult>> results) {
        return new CriteriaSectionViewPanel("criteriaPanel", results);
    }
}
