package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class EventFormEditPanel extends EventFormPanel {


    public EventFormEditPanel(String id, final IModel<? extends AbstractEvent> event, IModel<List<AbstractEvent.SectionResults>> results) {
        super(id, event, results);
    }

    @Override
    protected Panel getCriteriaSectionPanel(Class<? extends AbstractEvent> eventClass, PropertyModel<List<CriteriaResult>> results) {
        return new CriteriaSectionEditPanel("criteriaPanel", eventClass, results);
    }
}
