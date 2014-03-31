package com.n4systems.fieldid.wicket.components.event;


import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.SubEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class SubEventDetailsPanel extends Panel {

    @SpringBean
    private EventService eventService;

    public SubEventDetailsPanel(String id, IModel<SubEvent> model) {
        super(id);

        final IModel<SubEvent> subEventModel = Model.of(eventService.lookupExistingEvent(SubEvent.class, model.getObject().getId()));

        add(new ListView<String>("attribute", new PropertyModel<List<? extends String>>(subEventModel, "type.infoFieldNames")) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("label", item.getModel()));
                item.add(new Label("value", new PropertyModel<String>(subEventModel, "infoOptionMap[" + item.getModelObject() + "]")));
            }
        });

        add(new EventFormViewPanel("eventFormPanel", SubEvent.class, new PropertyModel<List<AbstractEvent.SectionResults>>(subEventModel, "sectionResults")));

        add(new Label("comments", new PropertyModel<String>(subEventModel, "comments")));

        add(new EventAttachmentsPanel("eventAttachmentsPanel", model));
    }
}