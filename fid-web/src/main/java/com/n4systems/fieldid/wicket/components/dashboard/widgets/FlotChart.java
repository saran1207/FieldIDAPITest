package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import com.n4systems.util.chart.ChartData;
import com.n4systems.util.json.JsonRenderer;

@SuppressWarnings("serial")
public class FlotChart<X> extends WebMarkupContainer {
	
	static AtomicInteger markupId = new AtomicInteger(1);
	
	private FlotOptions options;
	private JsonRenderer jsonRenderer = new JsonRenderer();  // TODO DD: springify this bean.
	
	public FlotChart(final String id, IModel<List<ChartData<X>>> model) {
		super(id, model);
		this.options = new FlotOptions();				
        setOutputMarkupId(true).setMarkupId(createNextMarkupId());
        
		add(new AbstractBehavior () {
			// TODO DD : not sure if this needs to be in renderHead or just hooked into render.????  which is better...
			@Override
			public void renderHead(IHeaderResponse response) {
				StringBuffer javascriptBuffer = new StringBuffer();
				javascriptBuffer.append ("dashboardWidgetFactory.createWithData('"+getMarkupId() + "'," + 
						getChartDataJavascriptString() + "," +   // TODO DD : use jsonRenderer for chartData too.
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
		Long panMin = null;
		Long panMax = null;
		Long min = null;
		for (Iterator<ChartData<X>> i = getChartData().iterator(); i.hasNext(); ) {
			ChartData<X> chartData = i.next();
			min = min(min, chartData.getMinX());
			panMin = min(panMin, chartData.getPanMin());	
			panMax = max(panMax, chartData.getPanMax());	
		}
		options.xaxis.min = min;
		options.xaxis.panRange = new Long[]{panMin, panMax};
		return options;
	}
	
	
	// TODO DD : put in utils? 
	private Long max(Long a, Long b) {
		// will return null if both are null. 
		return a==null ? b : 
			b==null ? a : 
				a.compareTo(b) < 0 ? b : a;  
	}
	private Long min(Long a, Long b) { 
		return a==null ? b : 
			b==null ? a : 
				a.compareTo(b) < 0 ? a : b;  
	}

	private String createNextMarkupId() {
		return "flotChart_"+markupId.getAndIncrement();
	}		
	
}
		
	
	
