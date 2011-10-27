package com.n4systems.util.chart;

import java.io.Serializable;

public interface ChartManager<X> extends Serializable {
	
	public Integer getLimit(); 		// trim down data above this threshold.
	public Long getMinX(ChartData<X> data);
	public Long getPanMin(ChartData<X> data);
	public Long getPanMax(ChartData<X> data);
	public Long getLongX(X x);
	public Chartable<X> preprocess(Chartable<X> chartable, int index);
// TODO DD : 	public ChartData<X> normalize(); 

}
