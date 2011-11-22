package com.n4systems.model.dashboard.widget.interfaces;

import com.n4systems.util.chart.ChartGranularity;

public interface ConfigurationWithGranularity {

    public ChartGranularity getGranularity();

    public void setGranularity(ChartGranularity granularity);

}
