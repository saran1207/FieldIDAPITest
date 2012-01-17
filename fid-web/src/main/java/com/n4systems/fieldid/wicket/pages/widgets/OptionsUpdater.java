package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.util.chart.FlotOptions;

public interface OptionsUpdater {

	public <X> FlotOptions<X> updateOptions(FlotOptions<X> options);
}
