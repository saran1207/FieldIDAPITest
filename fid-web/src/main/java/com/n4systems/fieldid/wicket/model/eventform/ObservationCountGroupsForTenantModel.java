package com.n4systems.fieldid.wicket.model.eventform;

import com.n4systems.fieldid.service.event.ObservationCountService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.ObservationCountGroup;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ObservationCountGroupsForTenantModel extends FieldIDSpringModel<List<ObservationCountGroup>>{

    @SpringBean
    private ObservationCountService observationCountService;

    @Override
    protected List<ObservationCountGroup> load() {
        return observationCountService.getObservationCountGroups();
    }
}
