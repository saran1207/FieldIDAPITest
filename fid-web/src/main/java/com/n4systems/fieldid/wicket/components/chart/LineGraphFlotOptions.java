package com.n4systems.fieldid.wicket.components.chart;

import java.util.Iterator;
import java.util.List;

import com.n4systems.util.chart.ChartData;


@SuppressWarnings("serial")
public class LineGraphFlotOptions<X> extends FlotOptions<X> {


	public LineGraphFlotOptions() { 
		super();
		grid.height = 200;
		points.show = true;
		lines.show = true;
		xaxis.min = Long.MAX_VALUE;
		xaxis.mode = "time";
		xaxis.timeFormat = "%b %d, %y";
		xaxis.monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};				
		
		yaxis.decimals = "0";
		
		grid.hoverable = Boolean.TRUE;
		grid.clickable = Boolean.TRUE;
		
		pan.interactive = true;
	}

	@Override
	public FlotOptions<X> update(List<ChartData<X>> list) {
		Long panMin = null;
		Long panMax = null;
		Long min = null;
		for (Iterator<ChartData<X>> i = list.iterator(); i.hasNext(); ) {
			ChartData<X> chartData = i.next();
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
