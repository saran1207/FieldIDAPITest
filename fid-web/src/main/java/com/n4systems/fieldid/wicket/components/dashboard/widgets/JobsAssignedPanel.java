package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.dashboard.subcomponents.LinkToJob;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.jobs.OpenJobsForUserDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Project;

public class JobsAssignedPanel extends Panel {

    public JobsAssignedPanel(String id) {
        super(id);

        add(CSSPackageResource.getHeaderContribution("style/dashboard/widgets/jobsassigned.css"));

        List<IColumn> columns = new ArrayList<IColumn>();
        columns.add(new PropertyColumn<String>(new FIDLabelModel("label.job_id"), "projectID"));
        columns.add(new PropertyColumn<String>(new FIDLabelModel("label.title"), "name"));
        columns.add(new PropertyColumn<String>(new FIDLabelModel("label.status"), "status"));
        columns.add(createLinkColumn());
        IColumn[] columnsArray = columns.toArray(new IColumn[columns.size()]);

        final OpenJobsForUserDataProvider openJobsForUserDataProvider = new OpenJobsForUserDataProvider();

        NonWicketLink assignedJobsLink;
        add(assignedJobsLink = new NonWicketLink("assignedJobsLink", "jobs.action?justAssignedOn=true"));
        assignedJobsLink.add(new Label("totalJobs", openJobsForUserDataProvider.size() + ""));

        SimpleDataTable<Project> jobsTable = new SimpleDataTable<Project>("jobsTable", columnsArray, openJobsForUserDataProvider, 5) {
            @Override
            public boolean isVisible() {
                return openJobsForUserDataProvider.size() > 0;
            }
        };
        add(jobsTable);
        jobsTable.setDisplayPagination(false);
        jobsTable.setCssClass("simpleTable decorated");

        add(new WebMarkupContainer("noJobsAssignedMessage") {
            @Override
            public boolean isVisible() {
                return openJobsForUserDataProvider.size() == 0;
            }
        });
    }

    private IColumn createLinkColumn() {
        return new PropertyColumn(new Model<String>(""), "id") {
            @Override
            public void populateItem(Item item, String componentId, IModel rowModel) {
                item.add(new LinkToJob(componentId, new PropertyModel<Long>(rowModel, "id")));
            }
        };
    }

}
