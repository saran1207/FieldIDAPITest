package com.n4systems.util.chart;

import com.n4systems.util.math.MathUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChartData<X extends Comparable> extends ArrayList<ChartSeries<X>> {

    private ChartManager<X> manager = null;
    private boolean normalized = false;

    private ChartData(ChartManager<X> manager) {
        this.manager = manager;
	}

	public ChartData(ChartManager<X> manager, List<ChartSeries<X>> data) {
        this(manager);
		addAll(data);
	}

	public ChartData(ChartManager<X> manager, ChartSeries<X> chartSeries) {
        this(manager);
		add(chartSeries);
	}

    @Override
    public boolean add(ChartSeries<X> xChartSeries) {
        boolean b = super.add(xChartSeries);
        normalize();
        return b;
    }

    @Override
    public boolean addAll(Collection<? extends ChartSeries<X>> c) {
        boolean b = super.addAll(c);
        normalize();
        return b;
    }

    private void normalize() {
        // your chance to re-arrange the order of series.  (useful for Z-order type stuff..which series is drawn first or last etc..)
        manager.sortSeries(this);

        X firstX = getFirstX();
        X lastX = getLastX();
        for (ChartSeries<X> chartSeries:this) {
            manager.normalize(chartSeries, firstX, lastX);
        }
    }

    private X getFirstX() {
        X min = null;
        for (ChartSeries<X> chartSeries:this) {
            min = MathUtil.nullSafeMin(chartSeries.getFirstX(), min);
        }
        return min;
    }

    private X getLastX() {
        X max = null;
        for (ChartSeries<X> chartSeries:this) {
            max = MathUtil.nullSafeMax(chartSeries.getLastX(), max);
        }
        return max;
    }


    // your chance to update the options after the data has been returned.
    // for any static configuration of data, use the ChartWidget.createOptions() method.
    public FlotOptions<X> updateOptions(FlotOptions<X> options) {
        manager.updateOptions(this, options);

		int seriesIndex = 0;
		for (ChartSeries<X> chartSeries:this) {
			manager.updateOptions(chartSeries, options, seriesIndex++);
		}
		return options;
	}

}
