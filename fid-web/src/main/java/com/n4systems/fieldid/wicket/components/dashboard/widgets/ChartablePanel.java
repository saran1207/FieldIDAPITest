package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public abstract class ChartablePanel<X> extends Panel {

	public ChartablePanel(String id) { 
		super(id);				
		add(new FlotChart<X>("chart", new LoadableDetachableModel<List<ChartData<X>>>() {
			@Override protected List<ChartData<X>> load() {
				return getChartData();
			}			
		}));		
	}
	
	protected abstract List<ChartData<X>> getChartData();
	
		
	

}
