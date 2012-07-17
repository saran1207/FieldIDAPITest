package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.event.CloseEventPage;
import com.n4systems.model.Event;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class OpenActionsCell extends Panel {
    public OpenActionsCell(String id, IModel<Event> eventModel) {
        super(id);
        
        final Event schedule = eventModel.getObject();

        String startAction = "selectEventAdd.action?uniqueID=" + schedule.getId() + "&assetId=" + schedule.getAsset().getId() + "&type=" + schedule.getType().getId();
        add(new NonWicketLink("startLink", startAction, new AttributeModifier("class", "mattButtonLeft")));

        add(new Link("closeLink") {
            @Override
            public void onClick() {
                setResponsePage(new CloseEventPage(PageParametersBuilder.uniqueId(schedule.getId()), new AssetEventsPage(PageParametersBuilder.uniqueId(schedule.getAsset().getId()))));
            }
        });

        add(new NonWicketLink("deleteLink", "eventDelete.action?uniqueID=" + schedule.getId() + "&assetId=" + schedule.getAsset().getId()));
    }
}
