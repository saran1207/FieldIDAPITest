package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.data.EventReportDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequest;

import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ReportResultsPanel extends SRSResultsPanel<EventReportCriteriaModel> {

    public ReportResultsPanel(String id, final IModel<EventReportCriteriaModel> eventCriteriaModel) {
        super(id, eventCriteriaModel);
    }

    @Override
    protected IColumn<RowView> createActionsColumn() {
        return new EventActionsColumn();
    }

    @Override
    protected FieldIdAPIDataProvider createDataProvider(IModel<EventReportCriteriaModel> criteriaModel) {
        return new EventReportDataProvider(criteriaModel.getObject());
    }

    @Override
    protected void storeCriteriaIfNecessary() {
        HttpSession session = ((WebRequest) getRequest()).getHttpServletRequest().getSession();
        new LegacyReportCriteriaStorage().storeCriteria(criteriaModel.getObject(), session);
    }

}
