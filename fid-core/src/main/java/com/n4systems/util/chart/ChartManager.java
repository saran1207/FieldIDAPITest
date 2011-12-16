package com.n4systems.util.chart;

import java.io.Serializable;

public interface ChartManager<X> extends Serializable {
	
	public Long getMinX(ChartSeries<X> series);
	public Long getPanMin(ChartSeries<X> series);
	public Long getPanMax(ChartSeries<X> series);
	public Long getLongX(X x);
	public ChartSeries<X> normalize(ChartSeries<X> series);
	public void updateOptions(ChartSeries<X> chartSeries, FlotOptions<X> options, int index);
}
