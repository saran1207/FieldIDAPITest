package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.wicket.pages.event.ThingEventPage;
import org.apache.wicket.Component;

public abstract class MasterEventPage extends ThingEventPage {

    @Override
    protected Component createSubHeader(String subHeaderId) {
        return new MasterEventSubHeader(subHeaderId, event);
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new StartSubEventActionGroup(actionGroupId, event);
    }
}
