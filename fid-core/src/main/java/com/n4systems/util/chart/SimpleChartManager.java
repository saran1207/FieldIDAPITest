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


	
}
