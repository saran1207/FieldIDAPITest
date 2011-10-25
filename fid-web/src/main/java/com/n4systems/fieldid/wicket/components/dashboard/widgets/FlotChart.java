package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import com.n4systems.util.chart.ChartData;
import com.n4systems.util.json.JsonRenderer;

@SuppressWarnings("serial")
public class FlotChart<X> extends WebMarkupContainer {
	
	private FlotOptions options;
	private JsonRenderer jsonRenderer = new JsonRenderer();  // TODO DD: springify this bean.
	
	public FlotChart(final String id, IModel<List<ChartData<X>>> model) {
		super(id, model);
		this.options = new FlotOptions();
		
        setOutputMarkupId(true).setMarkupId(getId());
		add(new AbstractBehavior () {
			// TODO DD : not sure if this needs to be in renderHead or just hooked into render.????  which is better...
			@Override
			public void renderHead(IHeaderResponse response) {
				StringBuffer javascriptBuffer = new StringBuffer();
				javascriptBuffer.append ("dashboardWidgetFactory.createWithData('"+id + "'," + 
						getChartDataJavascriptString() + "," +   // TODO DD : use jsonRenderer for this object too.
						jsonRenderer.render(getUpdatedOptions()) + 
						");");
				response.renderOnLoadJavascript(javascriptBuffer.toString());
			}

			//	e.g.   [{data:[[3,6.4]], label:text}, {data:[[4,9.0],[6,2]]} ]
			// the stuff in curly brackets are handled by chartData class.
			protected String getChartDataJavascriptString() {
				StringBuffer buff = new StringBuffer("[");		
				for (Iterator<ChartData<X>> i = getChartData().iterator(); i.hasNext(); ) {
					ChartData<X> chartData = i.next();
					buff.append(chartData.toJavascriptString());
					buff.append(i.hasNext() ? "," : "]");
				}
				return buff.toString();		
			}

		});
	}    	
	    	
	@SuppressWarnings("unchecked")
	private List<ChartData<X>> getChartData() {
		return ((List<ChartData<X>>)getDefaultModelObject());
	}
	
	public void setOptions(FlotOptions options) { 
		this.options = options;
	}
	
	public FlotOptions getOptions() { 
		return options;
	}

	// some options get updated depending on the data results...this is the hook to change them.
	// otherwise you can just use   myFlotChart.getOptions().hoverable = true  for example.
	private FlotOptions getUpdatedOptions() {
		long panMin = Long.MAX_VALUE;
		long panMax = Long.MIN_VALUE;
		long min = Long.MAX_VALUE;
		for (Iterator<ChartData<X>> i = getChartData().iterator(); i.hasNext(); ) {
			ChartData<X> chartData = i.next();
			min = Math.min(min,chartData.getMinX());
			panMin = Math.min(panMin, chartData.getPanRange()[0]);	// TODO DD : make null safe.
			panMax = Math.max(panMax, chartData.getPanRange()[1]);	// TODO DD : make null safe.
		}
		options.xaxis.min = min;
		options.xaxis.panRange = new Long[]{panMin, panMax};
		return options;
	}	
		
}
		
	
	
