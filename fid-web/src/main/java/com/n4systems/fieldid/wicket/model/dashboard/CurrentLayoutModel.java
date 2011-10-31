package com.n4systems.fieldid.wicket.model.dashboard;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.services.dashboard.DashboardService;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CurrentLayoutModel extends FieldIDSpringModel<DashboardLayout> {

    @SpringBean
    private DashboardService dashboardService;

    @Override
    protected DashboardLayout load() {
        return dashboardService.findLayout();
    }

}
