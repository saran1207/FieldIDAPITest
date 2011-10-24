package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public abstract class ChartablePanel<X> extends Panel {

	public ChartablePanel(String id) { 
		super(id);
		add(new Flot("graph"));  // TODO DD : rename "graph"-->"chart" for consistency.		
	}
	
	protected abstract List<ChartData<X>> getChartData();
	
	
	
    class Flot extends WebMarkupContainer {
    	
    	public Flot(final String id) {
    		super(id, new LoadableDetachableModel<List<ChartData<X>>>() {
				@Override protected List<ChartData<X>> load() {
					return getChartData();
				}			
    		});
            setOutputMarkupId(true).setMarkupId(getId());
			add(new AbstractBehavior () {
				@Override
				public void renderHead(IHeaderResponse response) {
					StringBuffer javascriptBuffer = new StringBuffer();
					// FIXME DD : use bogus variable name for now.
					javascriptBuffer.append ("var foo = dashboardWidgetFactory.createWithData('"+id + "'," + getChartDataJavascriptString()+");");
					response.renderOnLoadJavascript(javascriptBuffer.toString());
				}

				//	e.g.   [{data:[[3,6.4]], label:text}, {data:[[4,9.0],[6,2]]} ]
				// the stuff in curly brackets are handled by chartData class.
				protected String getChartDataJavascriptString() {
					StringBuffer buff = new StringBuffer("[");		
					for (Iterator<ChartData<X>> i = ((List<ChartData<X>>)getDefaultModelObject()).iterator(); i.hasNext(); ) {
						ChartData<X> chardData = i.next();
						buff.append(chardData.toJavascriptString());
						buff.append(i.hasNext() ? "," : "]");
					}
					return buff.toString();		
				}
				

			});
    	}    	
    	    	
    }
	

}
