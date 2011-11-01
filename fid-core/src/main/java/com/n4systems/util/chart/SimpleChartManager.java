package com.n4systems.util.chart;


@SuppressWarnings("serial")
public class SimpleChartManager<X> implements ChartManager<X> {

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
		if (index==0) {	//reset every request.
			options.xaxis.min = null;
			options.xaxis.panRange=new Long[2]; 				
		}
		options.xaxis.min = min(options.xaxis.min, getMinX(chartSeries));
		if (options.pan.interactive) { 
			options.xaxis.panRange[0] = min(options.xaxis.panRange[0], getPanMin(chartSeries));
			options.xaxis.panRange[1] = max(options.xaxis.panRange[1], getPanMax(chartSeries));
		}
	}
	

	// ------------------------------------------------------------------------------------------------
	// TODO DD : put in utils class somewhere? 
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
