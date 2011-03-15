/**
 * 
 */
package com.n4systems.fieldid.reporting.helpers;

import com.n4systems.fieldid.viewhelpers.ColumnMappingView;

final class NoFilterReportColumnFilter implements ReportColumnFilter {
	public boolean available(ColumnMappingView columnMapping) {
		return true;
	}
}