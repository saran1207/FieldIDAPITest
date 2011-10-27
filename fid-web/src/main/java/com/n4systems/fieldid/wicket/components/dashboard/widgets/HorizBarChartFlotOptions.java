package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.List;
import java.util.Map.Entry;

import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.Chartable;


@SuppressWarnings("serial")
public class HorizBarChartFlotOptions<X> extends FlotOptions<X> {
	
	public HorizBarChartFlotOptions() {
		series = null;
		bars.barWidth = 0.5;
		bars.horizontal = false;
		bars.show = true;
	}
	
	
	@Override
	public FlotOptions<X> update(List<ChartData<X>> list) {
		// CAVEAT : this only handles one chart...not sure what to do for multiple.
		ChartData<X> chartData = list.iterator().next();
		xaxis.ticks = new String[chartData.size()][2];
		int i = 0;
		for (Entry<X, Chartable<X>> entry: chartData.getEntrySet()) {
			xaxis.ticks[i][0] = entry.getValue().getLongX() + "";
			xaxis.ticks[i][1] = entry.getValue().getX() + "";
			i++;
		}
		return this;
	}
	 
}
