package com.n4systems.fieldid.wicket.model.reporting;

import com.n4systems.fieldid.reporting.service.EventColumnsService;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ReportConfiguration;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;

import java.util.List;

public class EventReportDisplayColumnsModel extends FieldIDSpringModel<List<ColumnMappingGroupView>> {

    @Override
    protected List<ColumnMappingGroupView> load() {
        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
        return reportConfiguration.getColumnGroups();
    }

}
