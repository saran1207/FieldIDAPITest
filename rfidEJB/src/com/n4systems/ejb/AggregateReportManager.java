package com.n4systems.ejb;

import java.util.List;

import com.n4systems.util.AggregateReport;

public interface AggregateReportManager {
	public AggregateReport createAggregateReport(List<Long> inspectionIds);
}
