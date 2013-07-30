package com.n4systems.util.chart;

import java.util.ArrayList;

public class CriteriaTrendsChartManager extends BarChartManager {

    public CriteriaTrendsChartManager() {
        withNoThreshold();
    }

    @Override
    public void updateOptions(ChartSeries<String> chartSeries, FlotOptions<String> options, int seriesIndex, int maxChartSeries, int seriesCount) {
        if (options.xaxis.ticks==null) {
            options.xaxis.ticks = new String[maxChartSeries][2];
        }
        ArrayList<Chartable<String>> chartables = new ArrayList<Chartable<String>>(chartSeries.values());

        options.bars.horizontal = false;

        int i = 0;
        for (Chartable<String> value : chartables) {
            if (seriesIndex==0) {  // only need to set the labels one time.
                options.xaxis.ticks[i][0] = i+"";
                options.xaxis.ticks[i][1] = value.getX();
            }
            i++;
        }

        options.bars.align="center";
        options.bars.barWidth=0.5;
        options.xaxis.min = -1L;
        options.xaxis.max = 8L;
        options.xaxis.labelWidth = 105;
        options.pan.interactive=true;
        options.xaxis.panRange=new Long[2];
        options.xaxis.panRange[0] = getPanMin(chartSeries);
        options.xaxis.panRange[1] = getPanMax(chartSeries);
    }

    @Override
    public Long getPanMin(ChartSeries<String> data) {
        return 0L;
    }

    @Override
    public Long getPanMax(ChartSeries<String> data) {
        return new Long(data.size());
    }
}
