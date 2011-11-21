package com.n4systems.util.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Joiner;

@SuppressWarnings("serial")
public class BarChartManager implements ChartManager<String> {
	
	public static final String OTHER_BAR_NAME = "Other";
	
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
		return createChartable(chartable.getX(), chartable.getY(), index, null);
	}

	private Chartable<String> createChartable(String x, Number y, long index, String tooltip) {
		return !transpose ? 
				new StringChartable(x, y, index, tooltip) : 
				new StringChartable(x, index, y.longValue(), tooltip);		
	}
	
	@Override
	public void normalize(ChartSeries<String> series) {
		long index = 0;
		List<Chartable<String>> other = new ArrayList<Chartable<String>>();
		Double otherTotalValue = 0.0;		
		Double total = series.sumY();
		String otherFormat = "%1$s:%2$d";
		List<String> otherTooltip = new ArrayList<String>();
		
		for (Entry<String, Chartable<String>> entry:series.getEntrySet()) {
			Chartable<String> chartable = entry.getValue();
			double pct = chartable.getY().doubleValue() / total;
			if (pct < otherThreshold) {
				otherTooltip.add(String.format(otherFormat, chartable.getX(), chartable.getY()));
				other.add(chartable);
				otherTotalValue += chartable.getY().doubleValue();
			} else { 
				series.add(createChartable(chartable,index++));
			}
		}
		series.remove(other);		
		Joiner joiner = Joiner.on(", ").skipNulls();
		series.add(createChartable(OTHER_BAR_NAME, otherTotalValue, index++, joiner.join(otherTooltip)));				
	}

	@Override
	public void updateOptions(ChartSeries<String> chartSeries, FlotOptions<String> options, int index) {
		// CAVEAT : this only works for a single chartSeries...if you are plotting more than one code will need to be refactored to handle that.
		options.yaxis.ticks = new String[chartSeries.size()][3];
		int i = 0;
		for (Entry<String, Chartable<String>> entry: chartSeries.getEntrySet()) {			
			StringChartable value = (StringChartable)entry.getValue();
			options.yaxis.ticks[i][0] = i+"";
			options.yaxis.ticks[i][1] = entry.getValue().getX();
			options.yaxis.ticks[i][2] = value.getTooltip();
			i++;
		}
	}


}
