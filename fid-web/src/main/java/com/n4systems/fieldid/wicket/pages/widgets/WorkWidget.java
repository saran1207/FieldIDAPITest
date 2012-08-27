package com.n4systems.fieldid.wicket.pages.widgets;

import com.google.common.base.Joiner;
import com.n4systems.fieldid.wicket.components.Agenda;
import com.n4systems.fieldid.wicket.pages.widgets.config.WorkConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WorkWidgetConfiguration;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class WorkWidget extends Widget<WorkWidgetConfiguration> {

    private @SpringBean DashboardReportingService dashboardReportingService;

    public WorkWidget(String id, WidgetDefinition<WorkWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WorkWidgetConfiguration>>(widgetDefinition));
        add(new Agenda("agenda", new PropertyModel<WorkWidgetConfiguration>(widgetDefinition, "config")) {
            @Override protected EventReportCriteria createCriteria() {
                EventReportCriteria criteria = dashboardReportingService.convertWidgetDefinitionToReportCriteria(getWidgetDefinition().getObject().getId(), 0L, "");
                return criteria;
            }
        });
	}

	@Override
    public Component createConfigPanel(String id) {
		IModel<WorkWidgetConfiguration> configModel = new Model<WorkWidgetConfiguration>(getWidgetDefinition().getObject().getConfig());
		return new WorkConfigPanel(id, configModel);
	}

    @Override
    protected IModel<String> getSubTitleModel() {
        Joiner joiner = Joiner.on(" / ").skipNulls();
        return Model.of(joiner.join(getUserString(), getOrgString(), getEventTypeString(), getAssetTypeString()));
    }

    private String getAssetTypeString() {
        WorkWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getAssetType()!=null ? config.getAssetType().getDisplayName() : null;
    }

    private String getEventTypeString() {
        WorkWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getEventType()!=null ? config.getEventType().getDisplayName() : null;
    }

    private String getOrgString() {
        WorkWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getOrg()!=null ? config.getOrg().getDisplayName() : null;
    }

    private String getUserString() {
        WorkWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getUser()!=null ? config.getUser().getDisplayName() : null;
    }
}
