package com.n4systems.fieldid.reporting.helpers;

import com.n4systems.model.search.ColumnMappingView;

public interface ReportColumnFilter {

	public boolean available(ColumnMappingView columnMapping);

}
