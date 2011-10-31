package com.n4systems.util.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


@SuppressWarnings("serial")
public class StringChartManager implements ChartManager<String> {
	
	private boolean transpose;
	private double otherThreshold = 0.02;

	public StringChartManager() { 
	}
	
	public StringChartManager(boolean transpose) { 
		this.transpose = transpose;
	}
	
	public StringChartManager(boolean transpose, double otherThreshold) { 
		this.transpose = transpose;
		this.otherThreshold = otherThreshold;
	}
	
	@Override
	public Long getMinX(ChartSeries<String> data) {
		return null;
	}

	@Override
	public Long getPanMin(ChartSeries<String> data) {
		return null;
	}

	@Override
	public Long getPanMax(ChartSeries<String> data) {
		return null;
	}

	@Override
	public Long getLongX(String x) {
		return null;
	}

	private Chartable<String> createChartable(Chartable<String> chartable, long index) {
		return createChartable(chartable.getX(), chartable.getY(), index);
	}

	private Chartable<String> createChartable(String x, Number y, long index) {
		return !transpose ? 
				new StringChartable(x, y, index) : 
				new StringChartable(x, index, y.longValue());		
	}
	
	@Override
	public void normalize(ChartSeries<String> series) {
		long index = 0;
		List<Chartable<String>> underThreshold = new ArrayList<Chartable<String>>();
		Double underThresholdTotal = 0.0;		
		Double total = series.sumY();
		
		for (Entry<String, Chartable<String>> entry:series.getEntrySet()) {
			Chartable<String> chartable = entry.getValue();
			double pct = chartable.getY().doubleValue() / total;
			if (pct < otherThreshold) { 
				underThreshold.add(chartable);
				underThresholdTotal += chartable.getY().doubleValue();
			} else { 
				series.add(createChartable(chartable,index++));
			}
		}
		series.remove(underThreshold);
		// TODO DD : localize this title.
		series.add(createChartable("Other", underThresholdTotal, index++));				
	}

}
