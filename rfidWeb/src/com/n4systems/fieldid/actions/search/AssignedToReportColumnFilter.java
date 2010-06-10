/**
 * 
 */
package com.n4systems.fieldid.actions.search;

import com.n4systems.fieldid.reporting.helpers.ReportColumnFilter;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.model.ExtendedFeature;

final class AssignedToReportColumnFilter implements ReportColumnFilter {
	private final boolean assignedToEnabled;

	AssignedToReportColumnFilter(boolean assignedToEnabled) {
		this.assignedToEnabled = assignedToEnabled;
	}

	public boolean available(ColumnMapping columnMapping) {
		if (assignedToEnabled) {
			return true;
		}
		
		if (!columnMapping.needsAnExtendedFeature()) {
			return true;
		}
		return requiredExtendedFeatureIsNotAssignedTo(columnMapping);
	}

	private boolean requiredExtendedFeatureIsNotAssignedTo(ColumnMapping columnMapping) {
		return ExtendedFeature.valueOf(columnMapping.getRequiredExtendedFeature()) != ExtendedFeature.AssignedTo;
	}
}