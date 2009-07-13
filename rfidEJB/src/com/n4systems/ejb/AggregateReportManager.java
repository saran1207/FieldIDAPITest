package com.n4systems.ejb;

import java.util.List;

import javax.ejb.Local;

import com.n4systems.util.AggregateReport;

@Local
public interface AggregateReportManager {
	public AggregateReport createAggregateReport(List<Long> inspectionIds);
}
