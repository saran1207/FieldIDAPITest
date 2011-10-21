package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public abstract class ChartablePanel<X> extends Panel {

	public ChartablePanel(String id) { 
		super(id);
		add(new Flot("graph"));  // TODO DD : rename "graph"-->"chart" for consistency.		
	}
	
	protected abstract ChartData<X> getChartData();
	
    class Flot extends WebMarkupContainer {
    	
    	public Flot(final String id) {
    		super(id);
            setOutputMarkupId(true).setMarkupId(getId());
			add(new AbstractBehavior () {
				@Override
				public void renderHead(IHeaderResponse response) {
					StringBuffer javascriptBuffer = new StringBuffer();
					javascriptBuffer.append ("dashboardWidgetFactory.createWithData('"+id + "'," + getChartData().toJavascriptString()+");");
					response.renderOnLoadJavascript(javascriptBuffer.toString());
				}
			});
    	}    	
    	    	
    }
	
	

}
