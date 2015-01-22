package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.ObservationCountGroup;

import java.util.List;

public class ObservationCountService extends FieldIdPersistenceService {

    public List<ObservationCountGroup> getObservationCountGroups() {
        return persistenceService.findAll(createTenantSecurityBuilder(ObservationCountGroup.class));
    }

}
