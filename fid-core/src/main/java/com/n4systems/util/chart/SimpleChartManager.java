package com.n4systems.util.chart;

@SuppressWarnings("serial")
public class SimpleChartManager<X> implements ChartManager<X> {

	@Override
	public Long getMinX(ChartData<X> data) {
		return  data.getFirstEntry().getLongX();
	}

	@Override
	public Long getPanMin(ChartData<X> data) {
		return null;
	}

	@Override
	public Long getPanMax(ChartData<X> data) {
		return null;
	}

	@Override
	public Integer getLimit() {
		return null;
	}

	@Override
	public Long getLongX(X x) {
		return null;
	}

	@Override
	public Chartable<X> preprocess(Chartable<X> chartable, int index) {
		return chartable;
	}
	
}
