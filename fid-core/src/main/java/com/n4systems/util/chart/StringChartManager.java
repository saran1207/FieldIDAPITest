package com.n4systems.util.chart;


@SuppressWarnings("serial")
public class StringChartManager implements ChartManager<String> {
	
	@Override
	public Integer getLimit() {
		return null;
	}

	@Override
	public Long getMinX(ChartData<String> data) {
		return null;
	}

	@Override
	public Long getPanMin(ChartData<String> data) {
		return null;
	}

	@Override
	public Long getPanMax(ChartData<String> data) {
		return null;
	}

	@Override
	public Long getLongX(String x) {
		return null;
	}

	@Override
	public Chartable<String> preprocess(Chartable<String> chartable, int index) {		
		return new StringChartable(chartable.getX(), chartable.getY(), (long) index);
	}

}
