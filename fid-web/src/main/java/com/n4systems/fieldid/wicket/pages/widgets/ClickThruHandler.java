package com.n4systems.fieldid.wicket.pages.widgets;

import java.io.Serializable;

import com.n4systems.util.chart.FlotOptions;

public interface ClickThruHandler extends OptionsUpdater, Serializable {

	@SuppressWarnings("serial")
	public static final ClickThruHandler NOP_CLICKTHRU_HANDLER = new ClickThruHandler() {
		@Override public <X> FlotOptions<X> updateOptions(FlotOptions<X> options) {
			options.fieldIdOptions.clickable = false;   //!!!not clickable.  you need to set this to true to enable click behaviour.
			return options;
		}

		@Override public String getUrl() {
			return "thisURLshouldNeverBeUsed"; // we've turn click handling off so url is moot. 
		}		
	};
	
	
	public String getUrl();

}
