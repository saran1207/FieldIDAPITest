package com.n4systems.util.chart;

import com.google.common.base.Joiner;

import java.util.*;
import java.util.Map.Entry;

public class BarChartManager extends SimpleChartManager<String> {
	
	public static final String OTHER_BAR_NAME = "Other";
	
	private boolean transpose;
	private double otherThreshold = 0.02;

    public BarChartManager() {
	}
	
	public BarChartManager(boolean transpose) { 
		this.transpose = transpose;
	}
	
	public BarChartManager(boolean transpose, double otherThreshold) { 
		this.transpose = transpose;
		this.otherThreshold = otherThreshold;
	}
	
	private Chartable<String> createChartable(Chartable<String> chartable, long index) {
		return createChartable(chartable.getX(), chartable.getY(), index, getTooltip(chartable));
	}

    protected String getTooltip(Chartable<String> chartable) {
        return null;
    }

    private Chartable<String> createChartable(String x, Number y, long index, String tooltip) {
		return !transpose ? 
				new StringChartable(x, y, index, tooltip) : 
				new StringChartable(x, index, y.longValue(), tooltip);		
	}
	
	@Override
	public ChartSeries<String> normalize(ChartSeries<String> series, String min, String max) {
        return normalizeOther(series, min, max);
	}

    public BarChartManager withNoThreshold() {
        otherThreshold = 0.0;
        return this;
    }

    private ChartSeries<String> normalizeOther(ChartSeries<String> series, String min, String max) {
        long index = 0;
        List<Chartable<String>> itemsClassifiedAsOther = new ArrayList<Chartable<String>>();
        Double totalValueOfOtherItems = 0.0;
        Double total = series.sumY();
        String otherFormat = "%1$s:%2$d";
        List<String> otherTooltip = new ArrayList<String>();

        List<Chartable<String>> standardChartables = new ArrayList<Chartable<String>>();

        for (Entry<String, Chartable<String>> entry:series.getEntrySet()) {
            Chartable<String> chartable = entry.getValue();
            double pct = chartable.getY().doubleValue() / total;
            if (pct < otherThreshold) {
                otherTooltip.add(String.format(otherFormat, chartable.getX(), chartable.getY()));
                itemsClassifiedAsOther.add(chartable);
                totalValueOfOtherItems += chartable.getY().doubleValue();
            } else {
                standardChartables.add(chartable);
            }
        }

        series.removeAll(itemsClassifiedAsOther);
        Joiner joiner = Joiner.on(", ").skipNulls();

        // WEB-2718 If we want "other" at the bottom, it has to be 0
        // If there are no "Others" and the first item has index 1, there's a gap rendered
        // we have to wait until we know that there are no "others" in order to add to the series.

        if (itemsClassifiedAsOther.size() > 0) {
            series.add(createChartable(OTHER_BAR_NAME, totalValueOfOtherItems, 0, joiner.join(otherTooltip)));
            index++;
        }

        for (Chartable<String> chartable : standardChartables) {
            series.add(createChartable(chartable,index++));
        }

        return series;
    }

    @Override
	public void updateOptions(ChartSeries<String> chartSeries, FlotOptions<String> options, int seriesIndex, int maxChartSeries, int seriesCount) {
        if (options.yaxis.ticks==null) {
            options.yaxis.ticks = new String[maxChartSeries][seriesCount+2];
        }
        ArrayList<Chartable<String>> chartables = new ArrayList<Chartable<String>>(chartSeries.values());

        options.bars.horizontal = transpose ? true : false;

        // Sort: WEB-2718 Flot is super sensitive about the order of the options -- which include the tooltip..
        // Since we stick the "Other" option at one end, but the chartable data sits in a TreeMap sorted by string
        // (which means that "Other" sits somewhere in the middle) we need to sort by Y value (in this case the actual index)
        // To get the options and the series data in true parallel.
        Collections.sort(chartables, new Comparator<Chartable<String>>() {
            @Override
            public int compare(Chartable<String> c1, Chartable<String> c2) {
                return new Integer(c1.getY().intValue()).compareTo(c2.getY().intValue());
            }
        });

		int i = 0;
        for (Chartable<String> value : chartables) {
            if (seriesIndex==0) {  // only need to set the labels one time.
			    options.yaxis.ticks[i][0] = value.getY()+"";
			    options.yaxis.ticks[i][1] = value.getX();
            }
			options.yaxis.ticks[i][2+seriesIndex] = ((StringChartable)value).getTooltip();
			i++;
		}

	}

}
