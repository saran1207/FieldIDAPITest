package com.n4systems.util.chart;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ChartData<X> extends ArrayList<ChartSeries<X>> {

	public ChartData() { 		
	}
	
	public ChartData(List<ChartSeries<X>> data) {
		addAll(data);
	}

	public ChartData(ChartSeries<X> chartSeries) {
		super();
		add(chartSeries);
	}

	public FlotOptions<X> updateOptions(FlotOptions<X> options) {
		int i = 0;
		for (ChartSeries<X> chartSeries:this) { 
			chartSeries.updateOptions(options, i++);
		}
		return options; 		
	}
	
}
