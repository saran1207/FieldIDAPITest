package com.n4systems.util.chart;


@SuppressWarnings("serial")
public class BarChartOptions<X> extends FlotOptions<X> {

	public BarChartOptions() {
		series = null;
		bars.barWidth = 0.5;
		bars.clickable = true;
		bars.show = true;
		bars.lineWidth = 0;
		yaxis.tickLength = 0;
		grid.show = true;
		grid.hoverable = true;
        points.show = false;
	}	
	
	 
}
