package com.n4systems.reporting.mapbuilders;

import com.n4systems.persistence.Transaction;
import com.n4systems.util.ReportMap;

public interface MapBuilder<T> {

	public void addParams(ReportMap<Object> params, T entity, Transaction transaction);
	
}
