package com.n4systems.fieldid.wicket.pages.widgets;

import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.chart.FlotChart;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithGranularity;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithPeriod;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.EnumUtils;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.LineGraphOptions;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * used to render a Flot Chart based widget.     
 *
 * the players are...
 * 
 * @ChartWidget is a widget that renders a FlotChart (which is wrapped in a lazy ajax panel).
 * @ChartSeries is a single graph.  				(e.g. if you had a chart of Apple & Google stocks, that would be two ChartSeries)
 * @ChartData is a collection of ChartSeries  	(in above example, that is one ChartData).
 * @ChartManager is responsible for manipulating ChartSeries data as it sees fit.  for example, it might round all scores down or remove all n/a event.  It also controls the scrolling/panning (or not) of chart.  
 * @FlotChart is a really simple charting component containing ChartData & options. (see see http://code.google.com/p/flot/) which is responsible for giving the javascript charting engine what it needs. 
 * @FlotOptions are the real meat of the FlotChart.  Think of this as a java object that matches all the options needed by the client side.
 * @JsonRenderer is responsible for doing Java->Json conversion (which is really Gson under the hood. http://code.google.com/p/google-gson/).
 * @WidgetConfiguration is base class to hold configuration for each different widget type.
 * 
 * steps to making a new flot chart based widget.  (prolly the easiest is to look at changeset 5486 and base it all on that.)
 * 
 *  - add your new widget to the enumeration.    see @WidgetType
 *  - create your extension of ChartWidget  (e.g. MyOrgsWidget extends ChartWidget & associated HTML)
 *  - in your widget's createOptions() method, fill in the required values. (e.g. set FlotOptions colors, chart type, etc... parameters)
 *  - create your WidgetConfiguration extension  (e.g. MyOrgsWidgetConfiguration).  ***make sure you implement the @WidgetConfiguration.copy() method!!!
 *  - create WidgetConfigurationPanel & HTML to make above object. 
 *  - implement your service call in DashboardReportingService.  it should return List<ChartSeries<?>>.   this result is returned in ChartWidget.getChartSeries() 
 *
 * see {@link http://fieldid.jira.com/wiki/display/WEB/Dashboard+Chart+Widgets} 
 * 
 */

@SuppressWarnings("serial")
public abstract class ChartWidget<X,T extends WidgetConfiguration> extends Widget<T> {

	private static final Logger logger = Logger.getLogger(ChartWidget.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	
	protected Component flotChart;
	protected ChartGranularity granularity;
	protected Integer period = 30;

	private ClickThruHandler clickThruHandler = ClickThruHandler.NOP_CLICKTHRU_HANDLER;
	private Set<OptionsUpdater> optionsUpdaters = Sets.newHashSet();

	@SpringBean
	private PersistenceService persistenceService;
	
	public ChartWidget(String id, IModel<WidgetDefinition<T>> model) {
		super(id, model);
		setOutputMarkupId(true);
		add(flotChart = createFlotChart());
		granularity = getDefaultGranularity();
        loadPeriodAndGranularityConfig();        
	}

    protected Component createFlotChart() {
		final String className = getClass().getSimpleName();
		return new AjaxLazyLoadPanel("chart") {
			@Override
			public Component getLazyLoadComponent(String markupId) {
				Component chart = createFlotChartImpl(markupId);
            	logger.warn("WIDGET_SOURCE end " + className + " @ " + dateFormat.format(new Date()));
				return chart;
			}
            @Override
			public Component getLoadingComponent(final String markupId) {
            	logger.warn("WIDGET_SOURCE start " + className + " @ " + dateFormat.format(new Date()));
                return new Label(markupId, "<div class='loadingText'>" + new FIDLabelModel("label.loading_ellipsis").getObject() +
                    "</div>").setEscapeModelStrings(false).add(new SimpleAttributeModifier("class", "chart-loading"));
            }
		};		
	}
	
    private void setGranularity(ChartGranularity granularity) {
    	if (granularity!=null && this.granularity.equals(granularity)) {
    		return;
    	}
        T config = getWidgetDefinition().getObject().getConfig();
        if (config instanceof ConfigurationWithGranularity) {
            ConfigurationWithGranularity configurationWithGranularity = (ConfigurationWithGranularity) config;
            configurationWithGranularity.setGranularity(granularity);
            persistenceService.update(config);
        }
        this.granularity = granularity;
    }        	
    
	public void setPeriod(Integer period) {
		if (period!=null && period.intValue()==this.period.intValue()) { 
			return;
		}
        T config = getWidgetDefinition().getObject().getConfig();
        if (config instanceof ConfigurationWithPeriod) {
            ConfigurationWithPeriod configurationWithPeriod = (ConfigurationWithPeriod) config;
            configurationWithPeriod.setPeriod(period);
            persistenceService.update(config);
        }
		this.period = period;
	}

	@SuppressWarnings("rawtypes")
	protected void addGranularityButton(final ChartGranularity granularity) {
        AjaxLink granularityButton = new AjaxLink(granularity.name().toLowerCase()) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setGranularity(granularity);
                target.add(ChartWidget.this);
            }
            @Override
            protected void onBeforeRender() {
           		setVisible(isGranularityAppicable(EnumUtils.valueOf(ChartGranularity.class, getMarkupId())));
            	super.onBeforeRender();
            }
            
        };
        granularityButton.setMarkupId(granularity.name());
    	granularityButton.setOutputMarkupId(true);
        granularityButton.add(new AttributeAppender("class", true, new Model<String>("selected"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return granularity == ChartWidget.this.granularity;
            }
        });
        
        add(granularityButton);
	}
	
	protected boolean isGranularityAppicable(ChartGranularity chartGranularity) {
		return true;
	}
	
	protected void addPeriodButton(String id, final int period) {
        AjaxLink<String> periodButton = new AjaxLink<String>(id) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setPeriod(period);
                target.add(ChartWidget.this);
            }
        };
        periodButton.add(new AttributeAppender("class", new Model<String>("selected"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return period == ChartWidget.this.period;
            }
        });
        add(periodButton);
	}

	private Component createFlotChartImpl(String id) {
		LoadableDetachableModel<ChartData<X>> model = new LoadableDetachableModel<ChartData<X>>() {
			@Override protected ChartData<X> load() {
				return getChartData();
			}
		};
		LoadableDetachableModel<FlotOptions<X>> optionsModel = new LoadableDetachableModel<FlotOptions<X>>() {
            @Override
            protected FlotOptions<X> load() {            	
                FlotOptions<X> options = createOptions();
                for (OptionsUpdater ou:optionsUpdaters) {
                	ou.updateOptions(options);
                }
                return options;
            }
        };
        return new FlotChart<X>(id, model, optionsModel, getFlotChartCss());
	}
	
	private String getFlotChartCss() {
		return getWidgetDefinition().getObject().getWidgetType().getCamelCase()+"Chart";
	}

	protected abstract ChartData<X> getChartData();
	
	protected FlotOptions<X> createOptions() {
		LineGraphOptions<X> options = new LineGraphOptions<X>();
        return options;
	}

	protected Class<? extends FieldIDFrontEndPage> getClickThroughPage() {
		return ReportPage.class;
	}

	protected ChartGranularity getDefaultGranularity() {
		return ChartGranularity.QUARTER;
	}
	
	protected IModel<T> getConfigModel() {
		IModel<T> configModel = new Model<T>(getWidgetDefinition().getObject().getConfig());
		return configModel;
	}

    private void loadPeriodAndGranularityConfig() {
        T config = getWidgetDefinition().getObject().getConfig();
        if (config instanceof ConfigurationWithGranularity) {
            setGranularity(validateGranularity( ((ConfigurationWithGranularity)config).getGranularity() ));
        }
        if (config instanceof ConfigurationWithPeriod) {
            setPeriod(((ConfigurationWithPeriod)config).getPeriod());
        }        
    }

	private ChartGranularity validateGranularity(ChartGranularity granularity) {
		ChartGranularity g = granularity;
		if (this instanceof HasDateRange) { 
			while (!isGranularityAppicable(g)) {
				g = g.finer();
				if (g==null) { 
					g = (ChartGranularity.DAY.equals(granularity)) ? ChartGranularity.WEEK : ChartGranularity.DAY;
					break;
				}
			}
		}
		setGranularity(g);
		return g;		
	}

	protected boolean isGranularityAppicable(ChartGranularity buttonGranularity, DateRange dateRange) {
		Duration duration = dateRange.getDuration();
		switch (buttonGranularity) {
			case DAY:
				return duration.getStandardDays()<100; 
			case WEEK:
				return duration.getStandardDays()>=14;												
			case MONTH:
				return duration.getStandardDays()>=60;
			case QUARTER:
				return duration.getStandardDays()>=120;				
			case YEAR: 
				return duration.getStandardDays()>=365*2;
		}
		return false;
	}
	
	protected Set<OptionsUpdater> add(OptionsUpdater optionsUpdater) { 
		optionsUpdaters.add(optionsUpdater);
		return optionsUpdaters;
	}		
	
	protected void setClickThruHandler(ClickThruHandler handler) {
		// note that this object may want a chance to set/override some options so hook it up.
		if (handler!=null) { 
			optionsUpdaters.remove(handler);			
		}
		this.clickThruHandler = handler;
		optionsUpdaters.add(handler);
	}
    
}
