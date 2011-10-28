package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.List;
import java.util.Map.Entry;

import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.Chartable;

//TODO DD : move this to wicket.pages pkg along with other stuff OR to general wicket.util.chart pkg?

@SuppressWarnings("serial")
public class HorizBarChartFlotOptions<X> extends FlotOptions<X> {
	
	public HorizBarChartFlotOptions() {
		series = null;
		bars.barWidth = 0.5;
		bars.horizontal = true;
		bars.show = true;
		yaxis.tickLength = 0;
		grid.show = Boolean.TRUE;
		grid.hoverable = Boolean.TRUE;
	}	
	
	@Override
	public FlotOptions<X> update(List<ChartData<X>> list) {
		// CAVEAT : this only handles one chart...not sure what to do for multiple.
		ChartData<X> chartData = list.iterator().next();
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
