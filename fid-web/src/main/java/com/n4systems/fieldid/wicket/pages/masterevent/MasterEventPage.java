package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.wicket.pages.event.ThingEventPage;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public abstract class MasterEventPage extends ThingEventPage {

    @Override
    protected Component createSubHeader(String subHeaderId) {
        return new MasterEventSubHeader(subHeaderId, event) {
            @Override
            protected void onEditSubEvent(IModel<ThingEvent> masterEventModel) {
                //Save the attached files if there are any
                masterEventModel.getObject().setAttachments(fileAttachments);
            }
        };
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new StartSubEventActionGroup(actionGroupId, event) {
            @Override
            protected void onPerformSubEvent(IModel<ThingEvent> masterEventModel) {
                //Save the attached files if there are any
                masterEventModel.getObject().setAttachments(fileAttachments);
            }
        };
    }
}
