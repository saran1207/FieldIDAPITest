package com.n4systems.util.chart;




@SuppressWarnings("serial")
public class LineGraphOptions<X> extends FlotOptions<X> {


	public LineGraphOptions() { 
		points.show = true;
		
		lines.show = true;
		lines.fill = true;
		lines.fillColor = "rgba(50, 87, 139, 0.20)";

		xaxis.min = Long.MAX_VALUE;
		xaxis.mode = "time";
		xaxis.timeformat = "%y/%m";
		xaxis.monthNames = MONTH_NAMES;				
		
		yaxis.decimals = "0";
		
		grid.hoverable = true;
		grid.clickable = true;
				
		pan.interactive = true;		
		
		legend.position = "nw";
		
		series.shadowSize = 0;
	}

}
