package com.n4systems.util.chart;


@SuppressWarnings("serial")
public class HorizBarChartOptions<X> extends FlotOptions<X> {
	
	public HorizBarChartOptions() {
		series = null;
		bars.barWidth = 0.5;
		bars.horizontal = true;
		bars.clickable = true;
		bars.show = true;
		bars.lineWidth = 0;
		yaxis.tickLength = 0;
		grid.show = true;
		grid.hoverable = true;
        points.show = false;
	}	
	
	 
}
