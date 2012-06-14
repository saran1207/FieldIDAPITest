package com.n4systems.fieldid.wicket.model.eventstatus;

import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventStatus;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventStatusesForTenantModel extends FieldIDSpringModel<List<EventStatus>> {

    @SpringBean
    private EventStatusService eventStatusService;

    @Override
    protected List<EventStatus> load() {
        return eventStatusService.getActiveStatues();
    }

}
