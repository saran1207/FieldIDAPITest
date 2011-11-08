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
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.json.JsonRenderer;

@SuppressWarnings("serial")
public class FlotChart<X> extends Panel {
	
	static AtomicInteger markupId = new AtomicInteger(1);
	
	private JsonRenderer jsonRenderer = new JsonRenderer();  // TODO DD: springify this bean.
    private IModel<FlotOptions<X>> optionsModel;

    public FlotChart(final String id, IModel<List<ChartSeries<X>>> model, IModel<FlotOptions<X>> optionsModel, String css) {
		super(id, model);
        this.optionsModel = optionsModel;
        add(new ChartMarkup("flot").add(new AttributeAppender("class", true, new Model<String>(css), " ")));
	}    	
	    	
	@SuppressWarnings("unchecked")
	private List<ChartSeries<X>> getChartSeries() {
		return ((List<ChartSeries<X>>)getDefaultModelObject());
	}

	class ChartMarkup extends WebMarkupContainer {

		public ChartMarkup(String id) {
			super(id);
	        setOutputMarkupId(true).setMarkupId(createNextMarkupId());        
	        setOutputMarkupPlaceholderTag(true);
			add(new AbstractBehavior () {
				@Override
				public void renderHead(IHeaderResponse response) {
                    optionsModel.detach();
					updateOptions(getChartSeries());					
					StringBuffer javascriptBuffer = new StringBuffer();
					javascriptBuffer.append ("chartWidgetFactory.createWithData('"+getMarkupId() + "'," + 
							jsonRenderer.render(getChartSeries()) + "," + 
							jsonRenderer.render(optionsModel.getObject()) +
					");");
					response.renderOnDomReadyJavascript(javascriptBuffer.toString());
				}

			});
			
		}
		
		private void updateOptions(List<ChartSeries<X>> list) {
			int i = 0;
			for (ChartSeries<X> chartSeries:list) { 
				chartSeries.updateOptions(optionsModel.getObject(), i++);
			}
		}
		
		private String createNextMarkupId() {
			return "flotChart_"+markupId.getAndIncrement();
		}		
		
	}

}
		
	
	
