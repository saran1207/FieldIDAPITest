package com.n4systems.util.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class BarChartManager implements ChartManager<String> {
	
	private boolean transpose;
	private double otherThreshold = 0.02;  // TODO DD : ask matt what proper threshold should be.

	public BarChartManager() { 
	}
	
	public BarChartManager(boolean transpose) { 
		this.transpose = transpose;
	}
	
	public BarChartManager(boolean transpose, double otherThreshold) { 
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

	@Override
	public void updateOptions(ChartSeries<String> chartSeries, FlotOptions<String> options, int index) {
		// CAVEAT : this only works for a single chartSeries...if you are plotting more than one code will need to be refactored to handle that.
		options.yaxis.ticks = new String[chartSeries.size()][2];
		int i = 0;
		for (Entry<String, Chartable<String>> entry: chartSeries.getEntrySet()) {			
			options.yaxis.ticks[i][0] = i+(options.bars.barWidth/2)+"";
			options.yaxis.ticks[i][1] = entry.getValue().getX()+"";
			i++;
		}
	}	

}
