package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.eventtypegroup.EventTypeGroupListLoader;

import java.util.ArrayList;
import java.util.List;

public class EventTypeGroupsForTenantModel extends FieldIDSpringModel<List<EventTypeGroup>> {

    @Override
    protected List<EventTypeGroup> load() {
//        return new ArrayList<EventTypeGroup>();
        return new EventTypeGroupListLoader(getSecurityFilter()).load();
    }

}
