package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.ActionsConfigPanel;
import com.n4systems.model.EventType;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.ActionsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.*;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ActionsWidget extends ChartWidget<String,ActionsWidgetConfiguration> implements HasDateRange {

	@SpringBean
	private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;

    public ActionsWidget(String id, WidgetDefinition<ActionsWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<ActionsWidgetConfiguration>>(widgetDefinition));
        setClickThruHandler(new ReportClickThruHandler(this,widgetDefinition.getId()));
    }
    
	@Override
    protected ChartData<String> getChartData() {
        ChartSeries<String> results = dashboardReportingService.getActions(getDateRange(), getOrg(), getUser(), getActionType());
        return new ChartData<String>(new BarChartManager(true), results);
    }

	@Override
	public DateRange getDateRange() {
        ActionsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return getTimeZoneDateRange(config.getRangeType());
	}

    private BaseOrg getOrg() {
        ActionsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getOrg();
    }

    private User getUser() {
        ActionsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getUser();
    }

    private EventType getActionType() {
        ActionsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getActionType();
    }

    @Override
    public Component createConfigPanel(String id) {
		return new ActionsConfigPanel(id, getConfigModel());
	}

	@Override
	protected ChartGranularity getDefaultGranularity() {
		return ChartGranularity.WEEK;   
	}	
	
	@Override
	protected boolean isGranularityAppicable(ChartGranularity buttonGranularity) {
		return isGranularityAppicable(buttonGranularity, getDateRange());
	}
	
	@Override
	protected IModel<String> getSubTitleModel() {
		SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), getOrg(), getDateRange().getRangeType());
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );		
	}
	
	@Override
	protected FlotOptions<String> createOptions() {
        HorizBarChartOptions<String> options = new HorizBarChartOptions<String>();
        return options;
	}
	
	@Override
	protected Class<? extends FieldIDFrontEndPage> getClickThroughPage() {
		return SearchPage.class;
	}
		
}



