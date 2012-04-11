package com.n4systems.fieldid.wicket.pages.widgets;


import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.*;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.AssetsStatusConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.AssetsStatusWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;

@SuppressWarnings("serial")
public class AssetsStatusWidget extends ChartWidget<String,AssetsStatusWidgetConfiguration>  implements HasDateRange {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
	@SpringBean 
	private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;
	
    public AssetsStatusWidget(String id, WidgetDefinition<AssetsStatusWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<AssetsStatusWidgetConfiguration>>(widgetDefinition));
		setClickThruHandler(new AssetClickThruHandler(this, widgetDefinition.getId()));		
    }

    @Override
    protected FlotOptions<String> createOptions() {
    	HorizBarChartOptions<String> options = new HorizBarChartOptions<String>();
    	return options;
    }

    @Override
	protected Class<? extends FieldIDFrontEndPage> getClickThroughPage() {
		return AssetSearchResultsPage.class;
	}

    
    
	@Override
    protected ChartData<String> getChartData() {
        BarChartManager chartManager = new BarChartManager(true);
        ChartSeries<String> results = reportingService.getAssetsStatus(getDateRange(), getOrg());
        return new ChartData<String>(chartManager, results);
    }

	private BaseOrg getOrg() {
		AssetsStatusWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}
		
	@Override
	public DateRange getDateRange() {
		AssetsStatusWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return new DateRange(config.getRangeType());
	}
	
	@Override
    public Component createConfigPanel(String id) {
		return new AssetsStatusConfigPanel(id,getConfigModel());
	}

	@Override
	protected IModel<String> getSubTitleModel() {
		SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), getOrg(), getDateRange().getRangeType());
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );		
	}

	
}



