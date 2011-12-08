package com.n4systems.util.chart;

import com.n4systems.util.time.DateUtil;



@SuppressWarnings("serial")
public class SimpleChartManager<X> implements ChartManager<X> {

	private static final int POINTS_THRESHOLD = 40;  //anything larger than this is considered an abundance of points. may affect rendering hints.

	@Override
	public Long getMinX(ChartSeries<X> data) {
		return  data.getFirstEntry().getLongX();
	}

	@Override
	public Long getPanMin(ChartSeries<X> data) {
		return null;
	}

	@Override
	public Long getPanMax(ChartSeries<X> data) {
		return null;
	}

	@Override
	public Long getLongX(X x) {
		return null;
	}

	@Override
	public void normalize(ChartSeries<X> series) {
		// do nothing.
	}
	
	@Override
	public void updateOptions(ChartSeries<X> chartSeries, FlotOptions<X> options, int index) {
		options.legend.noColumns = index+1;
		//reset first time thru. (recall this is invoked for every ChartSeries [0...n] that a chart is plotting.
		if (index==0) {	
			options.xaxis.min = null;
			options.xaxis.panRange=new Long[2]; 				
			options.points.radius = 3;
			options.points.show = true;
			options.lines.lineWidth = null;			
		}
		
		options.xaxis.min = DateUtil.nullSafeMin(options.xaxis.min, getMinX(chartSeries));
		if (options.pan.interactive) { 
			options.xaxis.panRange[0] = DateUtil.nullSafeMin(options.xaxis.panRange[0], getPanMin(chartSeries));
			options.xaxis.panRange[1] = DateUtil.nullSafeMax(options.xaxis.panRange[1], getPanMax(chartSeries));
		}
		options.points.radius = Math.min(options.points.radius, chartSeries.size()*(index+1)>POINTS_THRESHOLD?2:3);
	}
	
}
