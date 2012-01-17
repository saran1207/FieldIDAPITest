package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.AssetsIdentifiedConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.AssetsIdentifiedWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.FlotOptions;

@SuppressWarnings("serial")
public class AssetsIdentifiedWidget extends ChartWidget<LocalDate,AssetsIdentifiedWidgetConfiguration> implements HasDateRange {
	
	@SpringBean
	private DashboardReportingService reportingService;

	@SpringBean 
	private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;
	
    public AssetsIdentifiedWidget(String id, WidgetDefinition<AssetsIdentifiedWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<AssetsIdentifiedWidgetConfiguration>>(widgetDefinition));			
        addGranularityButton(ChartGranularity.YEAR);
        addGranularityButton(ChartGranularity.QUARTER);
        addGranularityButton(ChartGranularity.MONTH);
        addGranularityButton(ChartGranularity.WEEK);        
        addGranularityButton(ChartGranularity.DAY);
        setClickThruHandler(new AssetClickThruHandler(this,widgetDefinition.getId()));
    }
    
	@Override
    protected ChartData<LocalDate> getChartData() {
    	return new ChartData<LocalDate>(reportingService.getAssetsIdentified(getChartDateRange(), granularity, getOrg()));
    }

	@Override
	public ChartDateRange getChartDateRange() {
		AssetsIdentifiedWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getDateRange();
	}

	private BaseOrg getOrg() {
		AssetsIdentifiedWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}

	@Override
    public Component createConfigPanel(String id) {
		return new AssetsIdentifiedConfigPanel(id, getConfigModel());        
	}

	@Override
	protected ChartGranularity getDefaultGranularity() {
		return ChartGranularity.WEEK;   
	}	
	
	@Override
	protected boolean isGranularityAppicable(ChartGranularity buttonGranularity) {
		return isGranularityAppicable(buttonGranularity, getChartDateRange());
	}
	
	@Override
	protected IModel<String> getSubTitleModel() {
		SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), getOrg(), getChartDateRange());
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );		
	}
	
	@Override
	protected FlotOptions<LocalDate> createOptions() {
		FlotOptions<LocalDate> options = super.createOptions();
        return options;
	}
	
	@Override
	protected Class<? extends FieldIDFrontEndPage> getClickThroughPage() {
		return AssetSearchResultsPage.class;
	}
		
}



