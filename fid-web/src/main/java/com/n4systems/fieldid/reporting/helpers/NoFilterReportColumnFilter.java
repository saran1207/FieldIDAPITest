/**
 * 
 */
package com.n4systems.fieldid.reporting.helpers;

import com.n4systems.fieldid.viewhelpers.ColumnMapping;

final class NoFilterReportColumnFilter implements ReportColumnFilter {
	public boolean available(ColumnMapping columnMapping) {
		return true;
	}
}