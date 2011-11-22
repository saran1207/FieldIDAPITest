package com.n4systems.fieldid.wicket.pages.widgets;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithGranularity;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithPeriod;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.components.chart.FlotChart;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.LineGraphOptions;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public abstract class ChartWidget<X,T extends WidgetConfiguration> extends Widget<T> {

	private static final Logger logger = Logger.getLogger(ChartWidget.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	
	protected Component flotChart;
	protected ChartGranularity granularity;
	protected Integer period = 30;

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
            	logger.warn("WIDGET end " + className + " @ " + dateFormat.format(new Date()));				
				return chart;
			}
            @Override
			public Component getLoadingComponent(final String markupId) {
            	logger.warn("WIDGET start " + className + " @ " + dateFormat.format(new Date()));
                return new Label(markupId, "<div class='loadingText'>" + new FIDLabelModel("label.loading_ellipsis").getObject() +
                    "</div>").setEscapeModelStrings(false);
            }
		};		
	}
	
    private void setGranularity(ChartGranularity granularity) {
        T config = getWidgetDefinition().getObject().getConfig();
        if (config instanceof ConfigurationWithGranularity) {
            ConfigurationWithGranularity configurationWithGranularity = (ConfigurationWithGranularity) config;
            configurationWithGranularity.setGranularity(granularity);
        }
        persistenceService.update(config);
        this.granularity = granularity;
    }        	
    
	public void setPeriod(Integer period) {
        T config = getWidgetDefinition().getObject().getConfig();
        if (config instanceof ConfigurationWithPeriod) {
            ConfigurationWithPeriod configurationWithPeriod = (ConfigurationWithPeriod) config;
            configurationWithPeriod.setPeriod(period);
        }
        persistenceService.update(config);
		this.period = period;
	}

	@SuppressWarnings("rawtypes")
	protected void addGranularityButton(String id, final ChartGranularity granularity) {
        AjaxLink granularityButton = new AjaxLink(id) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setGranularity(granularity);
                target.addComponent(ChartWidget.this);
            }
        };
        granularityButton.add(new AttributeAppender("class", true, new Model<String>("selected"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return granularity == ChartWidget.this.granularity;
            }
        });
        add(granularityButton);
	}

	protected void addPeriodButton(String id, final int period) {
        AjaxLink periodButton = new AjaxLink(id) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setPeriod(period);
                target.addComponent(ChartWidget.this);
            }
        };
        periodButton.add(new AttributeAppender("class", true, new Model<String>("selected"), " ") {
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
                return createOptions();
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
        options.tooltipFormat = granularity == ChartGranularity.QUARTER ? FlotOptions.TOOLTIP_WITHOUT_DAY : FlotOptions.TOOLTIP_WITH_DAY;
        return options;
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
            this.granularity = ((ConfigurationWithGranularity)config).getGranularity();
        }
        if (config instanceof ConfigurationWithPeriod) {
            this.period = ((ConfigurationWithPeriod)config).getPeriod();
        }
    }

}
