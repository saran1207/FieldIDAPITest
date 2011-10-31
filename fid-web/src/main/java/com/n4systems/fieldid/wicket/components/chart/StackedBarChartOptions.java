package com.n4systems.fieldid.wicket.components.chart;

import java.util.Iterator;
import java.util.List;

import com.n4systems.util.chart.ChartSeries;


@SuppressWarnings("serial")
public class StackedBarChartOptions<X> extends FlotOptions<X> {

	public StackedBarChartOptions() { 
//		points.show = true;
		
		xaxis.mode = "time";
		xaxis.timeFormat = "%b %d, %y";
		xaxis.monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};				
		
		grid.clickable = true;
		grid.hoverable = true;
				
		series.stack = 0L;
		series.lines.show = false;
		series.lines.steps = false;
		
		series.bars.show = true;
		series.bars.barWidth = 24*60*60*1000*31.0;
		series.bars.align = "center";				
	}

	@Override
	public FlotOptions<X> update(List<ChartSeries<X>> list) {
		Long panMin = null;
		Long panMax = null;
		Long min = null;
		for (Iterator<ChartSeries<X>> i = list.iterator(); i.hasNext(); ) {
			ChartSeries<X> chartData = i.next();
			min = min(min, chartData.getMinX());
			panMin = min(panMin, chartData.getPanMin());	
			panMax = max(panMax, chartData.getPanMax());	
		}
		xaxis.min = min;
		if (panMin!=null && panMax!=null) {
			xaxis.panRange = new Long[]{panMin, panMax};
		}
		return this; 
	} 

	// TODO DD : put in utils? 
	private Long max(Long a, Long b) {
		// will return null if both are null. 
		return a==null ? b : 
			b==null ? a : 
				a.compareTo(b) < 0 ? b : a;  
	}
	
	private Long min(Long a, Long b) { 
		return a==null ? b : 
			b==null ? a : 
				a.compareTo(b) < 0 ? a : b;  
	}
	
	
}
