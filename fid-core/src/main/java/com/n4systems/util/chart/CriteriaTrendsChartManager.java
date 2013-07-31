package com.n4systems.util.chart;

import java.util.ArrayList;

public class CriteriaTrendsChartManager extends BarChartManager {

    public CriteriaTrendsChartManager() {
        withNoThreshold();
    }

    @Override
    public void updateOptions(ChartSeries<String> chartSeries, FlotOptions<String> options, int seriesIndex, int maxChartSeries, int seriesCount) {
        options.legend.show = false;
        options.bars.horizontal = false;

        options.yaxis.show=false;
        options.xaxis.show=false;
        options.xaxis.fieldidLabels = new String[maxChartSeries];

        // Though we are not displaying either axis, we have to set some tick data so the tooltips will work.

        ArrayList<Chartable<String>> chartables = new ArrayList<Chartable<String>>(chartSeries.values());

        int i = 0;
        for (Chartable<String> value : chartables) {
            options.xaxis.fieldidLabels[i++] = value.getX();
        }

        options.bars.align="center";
        options.bars.barWidth=0.9;
        options.xaxis.min = -1L;
        options.xaxis.max = 24L;
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
