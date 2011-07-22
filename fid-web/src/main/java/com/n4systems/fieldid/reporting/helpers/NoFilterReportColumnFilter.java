/**
 * 
 */
package com.n4systems.fieldid.reporting.helpers;

import com.n4systems.model.search.ColumnMappingView;

final class NoFilterReportColumnFilter implements ReportColumnFilter {
	public boolean available(ColumnMappingView columnMapping) {
		return true;
	}
}