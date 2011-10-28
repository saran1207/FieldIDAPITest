package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.n4systems.fieldid.wicket.components.dashboard.widgets.FlotChart;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.FlotOptions;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.LineGraphFlotOptions;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public abstract class ChartWidget<X> extends Widget {

	protected Component flotChart;

	public ChartWidget(String id, IModel<WidgetDefinition> widgetDefinition) {
		super(id, widgetDefinition);
		add(flotChart = createFlotChart());		
	}

	protected Component createFlotChart() {
		return new AjaxLazyLoadPanel("chart") {
			@Override
			public Component getLazyLoadComponent(String markupId) {
				return createFlotChartImpl(markupId);
			}
		};		
	}

	private final Component createFlotChartImpl(String id) {
		return 
		
		new FlotChart<X>(id, new LoadableDetachableModel<List<ChartData<X>>>() {
			@Override protected List<ChartData<X>> load() {
				return getChartData();
			}			
		}, createOptions());
	}
	
	protected abstract List<ChartData<X>> getChartData();
	
	protected FlotOptions<X> createOptions() {
		return new LineGraphFlotOptions<X>();
	}
	
}
