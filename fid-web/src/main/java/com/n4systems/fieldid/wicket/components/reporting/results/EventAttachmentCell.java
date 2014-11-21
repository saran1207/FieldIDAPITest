package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.model.ThingEvent;
import com.n4systems.util.views.RowView;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-11-20.
 */
public class EventAttachmentCell extends Panel {

    public EventAttachmentCell(String id, IModel<RowView> rowModel) {
        super(id);

        ThingEvent event = (ThingEvent) rowModel.getObject().getEntity();

        WebMarkupContainer actionsLink = new WebMarkupContainer("attachmentIcon");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new AttributeAppender("class", new Model<String>("icon-link"), ""));
        actionsLink.setVisible(!event.getAttachments().isEmpty());
        add(actionsLink);

    }
}
