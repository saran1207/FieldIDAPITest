package com.n4systems.fieldid.wicket.components.chart;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.json.JsonRenderer;

@SuppressWarnings("serial")
public class FlotChart<X> extends Panel {
	
	private static final Integer DEFAULT_CHART_HEIGHT = 200;

	static AtomicInteger markupId = new AtomicInteger(1);
	
	private FlotOptions<X> options;
	private JsonRenderer jsonRenderer = new JsonRenderer();  // TODO DD: springify this bean.
	private Integer height;

	public FlotChart(final String id, IModel<List<ChartSeries<X>>> model, FlotOptions<X> options, String css) {
		super(id, model);
		this.options = options;
		add(new ChartMarkup("flot").add(new AttributeAppender("class", true, new Model<String>(css), " ")));	                        				
	}    	
	    	
	@SuppressWarnings("unchecked")
	private List<ChartSeries<X>> getChartSeries() {
		return ((List<ChartSeries<X>>)getDefaultModelObject());
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
		return options.update(getChartSeries());
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
							jsonRenderer.render(getChartSeries()) + "," + 
							jsonRenderer.render(getUpdatedOptions()) + 
					");");
					response.renderOnDomReadyJavascript(javascriptBuffer.toString());
				}
			
			});
			
		}
		
		private String createNextMarkupId() {
			return "flotChart_"+markupId.getAndIncrement();
		}		

		
	}
	
}
		
	
	
