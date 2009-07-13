package com.n4systems.reporting;

import com.n4systems.util.ReportMap;

public class MapHolder {
	private final ReportMap<Object> reportMap;
	
	public MapHolder(ReportMap<Object> reportMap) {
		super();
		this.reportMap = reportMap;
	}

	public ReportMap<Object> getReportMap() {
		return reportMap;
	}
}
