package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.CssPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.dashboard.subcomponents.LinkToJob;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.jobs.OpenJobsForUserDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.model.Project;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;

public class JobsAssignedWidget extends Widget<WidgetConfiguration> {

    public JobsAssignedWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));

		List<IColumn<Project>> columns = new ArrayList<IColumn<Project>>();
        columns.add(new PropertyColumn<Project>(new FIDLabelModel("label.job_id"), "projectID"));
        columns.add(new PropertyColumn<Project>(new FIDLabelModel("label.title"), "name"));
        columns.add(new PropertyColumn<Project>(new FIDLabelModel("label.status"), "status"));
        columns.add(createLinkColumn());

        final OpenJobsForUserDataProvider openJobsForUserDataProvider = new OpenJobsForUserDataProvider();

        SimpleDataTable<Project> jobsTable = new SimpleDataTable<Project>("jobsTable", columns, openJobsForUserDataProvider, 5) {
            @Override
            public boolean isVisible() {
                return openJobsForUserDataProvider.size() > 0;
            }
        };
        jobsTable.setDisplayPagination(false);
        jobsTable.setCssClass("simpleTable decorated");

        EnclosureContainer jobsContainer = new EnclosureContainer("jobsAssignedEnclosure", jobsTable);
        jobsContainer.add(jobsTable);
        add(jobsContainer);

        NonWicketLink assignedJobsLink;
        jobsContainer.add(assignedJobsLink = new NonWicketLink("assignedJobsLink", "jobs.action?justAssignedOn=true"));
        assignedJobsLink.add(new Label("totalJobs", openJobsForUserDataProvider.size() + ""));

        add(new WebMarkupContainer("noJobsAssignedMessage") {
            @Override
            public boolean isVisible() {
                return openJobsForUserDataProvider.size() == 0;
            }
        });
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/dashboard/widgets/jobsassigned.css");
    }

    private IColumn<Project> createLinkColumn() {
        return new PropertyColumn<Project>(new Model<String>(""), "id") {
            @Override
            public void populateItem(Item item, String componentId, IModel rowModel) {
                item.add(new LinkToJob(componentId, new PropertyModel<Long>(rowModel, "id")));
            }
        };
    }

	@Override
    public Component createConfigPanel(String id) {
		IModel<WidgetConfiguration> configModel = new Model<WidgetConfiguration>(getWidgetDefinition().getObject().getConfig());		
		return new WidgetConfigPanel<WidgetConfiguration>(id, configModel);
	}

}
