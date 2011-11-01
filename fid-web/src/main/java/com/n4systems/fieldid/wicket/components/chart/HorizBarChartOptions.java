package com.n4systems.fieldid.wicket.components.chart;

import java.util.List;
import java.util.Map.Entry;

import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.Chartable;


@SuppressWarnings("serial")
public class HorizBarChartOptions<X> extends FlotOptions<X> {
	
	public HorizBarChartOptions() {
		series = null;
		bars.barWidth = 0.3;
		bars.horizontal = true;
		bars.show = true;
		yaxis.tickLength = 0;
		grid.show = Boolean.TRUE;
		grid.hoverable = Boolean.TRUE;
	}	
	
	@Override
	public FlotOptions<X> update(List<ChartSeries<X>> list) {
		// CAVEAT : this only handles one chart...not sure what to do for multiple.
		ChartSeries<X> chartData = list.iterator().next();
		yaxis.ticks = new String[chartData.size()][2];
		int i = 0;
		for (Entry<X, Chartable<X>> entry: chartData.getEntrySet()) {			
			yaxis.ticks[i][0] = i+"";
			yaxis.ticks[i][1] = entry.getValue().getX()+"";
			i++;
		}
		return this;
	}
	 
}
