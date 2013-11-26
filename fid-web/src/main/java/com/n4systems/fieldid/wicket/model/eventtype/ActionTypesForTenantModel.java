package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.service.event.ActionService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ActionTypesForTenantModel extends FieldIDSpringModel<List<ThingEventType>> {

    @SpringBean
    private ActionService actionService;

    @Override
    protected List<ThingEventType> load() {
        return actionService.getActionTypes();
    }
}
