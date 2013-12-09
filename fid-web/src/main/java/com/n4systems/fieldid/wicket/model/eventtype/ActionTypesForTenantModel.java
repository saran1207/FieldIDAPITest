package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.service.event.ActionService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.ActionEventType;
import com.n4systems.model.EventType;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ActionTypesForTenantModel extends FieldIDSpringModel<List<? extends EventType>> {

    @SpringBean
    private ActionService actionService;

    @Override
    protected List<ActionEventType> load() {
        return actionService.getActionTypes();
    }
}
