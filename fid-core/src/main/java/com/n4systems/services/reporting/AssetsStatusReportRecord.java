package com.n4systems.services.reporting;

import com.n4systems.util.chart.Chartable;
import com.n4systems.util.chart.StringChartable;


@SuppressWarnings("serial")
public class AssetsStatusReportRecord implements Chartable<String> {	

	private Chartable<String> chartable;
	private String status;
	private static long i=0;
	
	public AssetsStatusReportRecord(String status, final Long count) {
		this.status = status;
		chartable = new StringChartable(status, count, i);
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
