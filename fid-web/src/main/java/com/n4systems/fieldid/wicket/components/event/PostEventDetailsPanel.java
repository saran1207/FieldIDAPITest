package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.platform.PlatformInformationIcon;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.Event;
import com.n4systems.model.PlatformType;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.TimeZone;

public class PostEventDetailsPanel extends Panel {

    public PostEventDetailsPanel(String id, IModel<? extends Event> model) {
        super(id, model);

        TimeZone timeZone = FieldIDSession.get().getSessionUser().getTimeZone();

        add(new Label("comments", new PropertyModel(model, "comments")));
        add(new Label("created", new DayDisplayModel(new PropertyModel<Date>(model, "created"), true, timeZone)));
        add(new Label("createdBy", new PropertyModel(model, "createdBy.fullName")));
        add(new PlatformInformationIcon("createdIcon",
                new PropertyModel<PlatformType>(model, "createdPlatformType"),
                new PropertyModel<String>(model, "createdPlatform")));

        add(new Label("modified", new DayDisplayModel(new PropertyModel<Date>(model, "modified"), true, timeZone)));
        add(new Label("modifiedBy", new PropertyModel(model, "modifiedBy.fullName")));
        add(new PlatformInformationIcon("modifiedIcon",
                new PropertyModel<PlatformType>(model, "modifiedPlatformType"),
                new PropertyModel<String>(model, "modifiedPlatform")));

        if(model.getObject() instanceof ThingEvent) {
            add(new Label("assetStatus", new PropertyModel(model, "assetStatus")));
        } else {
            add(new Label("assetStatus").setVisible(false));
        }
    }
}
