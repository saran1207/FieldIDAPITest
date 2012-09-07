package com.n4systems.fieldid.wicket.model.event;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class PrioritiesForTenantModel extends FieldIDSpringModel<List<PriorityCode>> {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    @Override
    protected List<PriorityCode> load() {
        return priorityCodeService.getActivePriorityCodes();
    }

}
