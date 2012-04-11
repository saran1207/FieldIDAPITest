package com.n4systems.util.chart;

import com.n4systems.util.math.MathUtil;


public class SimpleChartManager<X extends Comparable> implements ChartManager<X> {


    @Override
    public Long getMin(ChartSeries<X> series) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
	public ChartSeries<X> normalize(ChartSeries<X> series, X min, X max) {
		// do nothing.
		return series;
	}

    @Override
    public void sortSeries(ChartData<X> chartSeries) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public void updateOptions(ChartSeries<X> chartSeries, FlotOptions<X> options, int index) {
        ;   // override to do specific series-by-series updating of options. (less common - use #updateOptions(data,options)
            //   for more general options manipulation.
	}

    @Override
    public void updateOptions(ChartData<X> data, FlotOptions<X> options) {
        options.legend.noColumns = data.size();
        options.xaxis.min = getMin(data);
        options.points.radius = getMaxPointsInASeries(data) > POINTS_THRESHOLD ? ChartManager.SMALL_RADIUS : LARGE_RADIUS;
        if (Boolean.TRUE.equals(options.pan.interactive)) {
            options.xaxis.panRange=new Long[2];
            options.xaxis.panRange[0] = getPanMin(data);
            options.xaxis.panRange[1] = getPanMax(data);
        }
    }


    private Long getPanMax(ChartData<X> data) {
        Long max = null;
        for (ChartSeries<X> chartSeries:data) {
            max = MathUtil.nullSafeMax(getPanMax(chartSeries), max);
        }
        return max;
    }

    private Long getPanMin(ChartData<X> data) {
        Long min = null;
        for (ChartSeries<X> chartSeries:data) {
            min = MathUtil.nullSafeMin(getPanMin(chartSeries), min);
        }
        return min;
    }

    private Long getMin(ChartData<X> data) {
        Long min = null;
        for (ChartSeries<X> chartSeries:data) {
            min = MathUtil.nullSafeMin(getMin(chartSeries), min);
        }
        return min;
    }

    public int getMaxPointsInASeries(ChartData<X> data) {
        int max = 0;
        for (ChartSeries<X> chartSeries:data) {
            max = Math.max(max, chartSeries.size());
        }
        return max;
    }
    
    


}
