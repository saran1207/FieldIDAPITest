package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.PopulatorLogBeanDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.PopulatorLogBean;
import rfid.web.helper.Constants;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DataLogPage extends SetupPage {

    PopulatorLogBeanDataProvider dataProvider;

    public DataLogPage(PageParameters params) {
        super(params);

        add(new SearchForm("searchForm"));

        add(new WebMarkupContainer("dataLogResultsTable").setVisible(false));
    }

    class SearchForm extends Form {

        private Date fromDate;
        private Date toDate;
        private PopulatorLog.logStatus status;
        private PopulatorLog.logType type;

        public SearchForm(String id) {
            super(id);

            add(new FIDFeedbackPanel("feedbackPanel"));

            List<PopulatorLog.logStatus> logStatuses = Arrays.asList(PopulatorLog.logStatus.values());
            List<PopulatorLog.logType> logTypes = Arrays.asList(PopulatorLog.logType.values());

            add(new DropDownChoice<PopulatorLog.logStatus>("logStatus", new PropertyModel<PopulatorLog.logStatus>(this, "status"), logStatuses).setNullValid(true));
            add(new DropDownChoice<PopulatorLog.logType>("logType", new PropertyModel<PopulatorLog.logType>(this, "type"), logTypes).setNullValid(true));
            add(new DateTimePicker("fromDate", new PropertyModel<Date>(this, "fromDate")));
            add(new DateTimePicker("toDate", new PropertyModel<Date>(this, "toDate")));
            add(new Button("runButton"));
        }

        @Override
        protected void onSubmit() {
            addDataTable(fromDate, toDate, status, type);
        }
    }

    protected void addDataTable(Date fromDate, Date toDate, PopulatorLog.logStatus status, PopulatorLog.logType type) {
        Long tenantId = FieldIDSession.get().getSessionUser().getTenant().getId();
        ISortableDataProvider<PopulatorLogBean> provider = new PopulatorLogBeanDataProvider(tenantId, fromDate,toDate,status,type);
        remove("dataLogResultsTable");
        add(new SimpleDataTable<PopulatorLogBean>("dataLogResultsTable", createColumns(),
                provider, Constants.PAGE_SIZE, "label.noresults", "label.emptydatalogentrylist"));
    }

    @SuppressWarnings("unchecked")
    private IColumn<PopulatorLogBean>[] createColumns() {
        return new IColumn[] {
                new PropertyColumn(new FIDLabelModel("label.time"), "timeLogged"),
                new PropertyColumn(new FIDLabelModel("label.message"), "logMessage"),
                new PropertyColumn(new FIDLabelModel("label.type"), "logType"),
                new PropertyColumn(new FIDLabelModel("label.status"), "logStatus"),
        };
    }

}
