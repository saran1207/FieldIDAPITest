package com.n4systems.fieldid.reporting.helpers;

import com.n4systems.fieldid.viewhelpers.ColumnMapping;

public interface ReportColumnFilter {

	public boolean available(ColumnMapping columnMapping);

}
