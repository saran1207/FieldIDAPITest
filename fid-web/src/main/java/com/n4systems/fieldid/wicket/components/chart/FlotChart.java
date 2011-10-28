package com.n4systems.fieldid.wicket.components.chart;


import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.util.chart.ChartData;
import com.n4systems.util.json.JsonRenderer;

@SuppressWarnings("serial")
public class FlotChart<X> extends Panel {
	
	private static final Integer DEFAULT_CHART_HEIGHT = 200;

	static AtomicInteger markupId = new AtomicInteger(1);
	
	private FlotOptions<X> options;
	private JsonRenderer jsonRenderer = new JsonRenderer();  // TODO DD: springify this bean.
	private Integer height;

	public FlotChart(final String id, IModel<List<ChartData<X>>> model, FlotOptions<X> options, String css) {
		super(id, model);
		this.options = options;
		this.height = options.grid.height !=null ? options.grid.height : DEFAULT_CHART_HEIGHT;
		add(new ChartMarkup("flot").add(new AttributeAppender("class", true, new Model<String>(css), " ")));	                        				
	}    	
	    	
	@SuppressWarnings("unchecked")
	private List<ChartData<X>> getChartData() {
		return ((List<ChartData<X>>)getDefaultModelObject());
	}
	
	public void setOptions(FlotOptions<X> options) { 
		this.options = options;
	}
	
	public FlotOptions<X> getOptions() { 
		return options;
	}

	// some options get updated depending on the data results...this is the hook to change them.
	// for static configuration you can just use   myFlotChart.getOptions().hoverable = true  for example.
	private FlotOptions<X> getUpdatedOptions() {
		return options.update(getChartData());
	}
	
	
	class ChartMarkup extends WebMarkupContainer {

		public ChartMarkup(String id) {
			super(id);
	        setOutputMarkupId(true).setMarkupId(createNextMarkupId());        
	        setOutputMarkupPlaceholderTag(true);
			add(new AbstractBehavior () {
				// TODO DD : not sure if this needs to be in renderHead or just hooked into render.????  which is better...
				@Override
				public void renderHead(IHeaderResponse response) {
					StringBuffer javascriptBuffer = new StringBuffer();
					javascriptBuffer.append ("dashboardWidgetFactory.createWithData('"+getMarkupId() + "'," + 
							getChartDataJavascriptString() + "," +   // TODO DD : use jsonRenderer for chartData just as it is used for options.
							jsonRenderer.render(getUpdatedOptions()) + 
					");");
					response.renderOnDomReadyJavascript(javascriptBuffer.toString());
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
		
		private String createNextMarkupId() {
			return "flotChart_"+markupId.getAndIncrement();
		}		

		
	}
	
}
		
	
	
