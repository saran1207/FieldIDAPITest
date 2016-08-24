package com.n4systems.ejb;

import com.n4systems.util.AggregateReport;

import java.util.List;

public interface AggregateReportManager {
	public AggregateReport createAggregateReport(List<Long> eventIds);
}
