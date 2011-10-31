package com.n4systems.services.reporting;

import com.n4systems.util.chart.AbstractChartable;
import com.n4systems.util.chart.Chartable;


@SuppressWarnings("serial")
public class AssetsStatusReportRecord implements Chartable<String> {	

	private Chartable<String> chartable;
	
	public AssetsStatusReportRecord(String status, final Long count) {
		chartable = new AbstractChartable<String>(status, count) {
			@Override public Long getLongX() {
				throw new IllegalStateException("this must be transformed by ChartManger because we don't know the LongX value until we have all the data collected/normalized.");
			}			
		};
	}
		
	@Override
	public String getX() {
		return chartable.getX();
	}

	@Override
	public Number getY() {
		return chartable.getY();
	}
	
	@Override
	public String toString() { 
		return chartable.toString();
	}

	@Override
	public String toJavascriptString() {
		return chartable.toJavascriptString();
	}
	
	@Override 
	public Long getLongX() {
		return chartable.getLongX();
	}


}
