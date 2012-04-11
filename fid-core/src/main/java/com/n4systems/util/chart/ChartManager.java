package com.n4systems.util.chart;

import java.io.Serializable;

public interface ChartManager<X extends Comparable> extends Serializable {

    public static final int POINTS_THRESHOLD = 40;  //anything larger than this is considered an abundance of points. may affect rendering hints.
    public static final int PAN_THRESHOLD = 95;

    public static final int SMALL_RADIUS = 2;
    public static final int LARGE_RADIUS = 3;

    Long getMin(ChartSeries<X> series);
	Long getPanMin(ChartSeries<X> series);
	Long getPanMax(ChartSeries<X> series);
	Long getLongX(X x);
	ChartSeries<X> normalize(ChartSeries<X> series, X min, X max);
	void updateOptions(ChartSeries<X> chartSeries, FlotOptions<X> options, int index);
    void updateOptions(ChartData<X> chartSeries, FlotOptions<X> options);
    void sortSeries(ChartData<X> chartSeries);
}
