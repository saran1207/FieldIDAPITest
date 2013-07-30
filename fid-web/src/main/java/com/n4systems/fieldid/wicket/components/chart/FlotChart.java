package com.n4systems.fieldid.wicket.components.chart;


import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("serial")
public class FlotChart<X extends Comparable> extends Panel {
	
	static AtomicInteger markupId = new AtomicInteger(1);
	
	@SpringBean
	private JsonRenderer jsonRenderer;

	private IModel<? extends FlotOptions<X>> optionsModel;

    private String tooltipFunction;
	
    public FlotChart(final String id, IModel<ChartData<X>> model, IModel<? extends FlotOptions<X>> optionsModel, String css) {
		super(id, model);
        this.optionsModel = optionsModel;
        add(new ChartMarkup("flot").add(new AttributeAppender("class", new Model<String>(css), " ")));
	}    	
	    	
	@SuppressWarnings("unchecked")
	private ChartData<X> getChartData() {
		return ((ChartData<X>)getDefaultModelObject());
	}
	
	protected void updateOptions(ChartData<X> chartData) {
		chartData.updateOptions(optionsModel.getObject());
	}

    public void setTooltipFunction(String functionName) {
        this.tooltipFunction = functionName;
    }

	class ChartMarkup extends WebMarkupContainer {

		public ChartMarkup(String id) {
			super(id);
	        setOutputMarkupId(true);        
	        setOutputMarkupPlaceholderTag(true);
		}

        @Override
        public void renderHead(IHeaderResponse response) {
            optionsModel.detach();
            updateOptions(getChartData());
            StringBuffer javascriptBuffer = new StringBuffer();
            String widgetName = "widget_" + getMarkupId();
            javascriptBuffer.append ("var " + widgetName + " = chartWidgetFactory.createWithData('"+getMarkupId() + "'," +
                    jsonRenderer.render(getChartData()) + "," +
                    jsonRenderer.render(optionsModel.getObject()) +
            ");");
            if (tooltipFunction != null) {
                javascriptBuffer.append(widgetName).append(".setTooltip(").append(tooltipFunction).append(");");
            }
            response.renderOnDomReadyJavaScript(javascriptBuffer.toString());
        }
    }

}
		
	
	
