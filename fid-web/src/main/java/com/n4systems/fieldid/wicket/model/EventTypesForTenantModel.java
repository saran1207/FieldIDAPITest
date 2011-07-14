package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.EventTypeListLoader;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventTypesForTenantModel extends FieldIDSpringModel<List<EventType>> {

    @SpringBean
    private PersistenceService persistenceService;

    @Override
    protected List<EventType> load() {
        return persistenceService.findAll(EventType.class);
    }

}
