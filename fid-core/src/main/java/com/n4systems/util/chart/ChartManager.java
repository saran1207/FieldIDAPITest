package com.n4systems.util.chart;

import java.io.Serializable;

public interface ChartManager<X> extends Serializable {
	
	public Integer getLimit(); 		// trim down data above this threshold.
	public Long getMinX(ChartSeries<X> data);
	public Long getPanMin(ChartSeries<X> data);
	public Long getPanMax(ChartSeries<X> data);
	public Long getLongX(X x);
	public Chartable<X> preprocess(Chartable<X> chartable, int index);
// TODO DD : 	public ChartSeries<X> normalize(); 

}
