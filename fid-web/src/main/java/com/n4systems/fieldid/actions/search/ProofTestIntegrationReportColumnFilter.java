package com.n4systems.fieldid.actions.search;

import com.n4systems.fieldid.reporting.helpers.ReportColumnFilter;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.model.ExtendedFeature;

public class ProofTestIntegrationReportColumnFilter implements ReportColumnFilter {

	private final boolean proofTestIntegrationEnabled;
	

	ProofTestIntegrationReportColumnFilter(boolean proofTestIntegrationEnabled) {
		this.proofTestIntegrationEnabled = proofTestIntegrationEnabled;
	}
	
	@Override
	public boolean available(ColumnMappingView columnMapping) {
		if (proofTestIntegrationEnabled) {
			return true;
		}
		
		if (!columnMapping.needsAnExtendedFeature()) {
			return true;
		}
		return requiredExtendedFeatureIsProofTestIntegration(columnMapping);
	}
	
	private boolean requiredExtendedFeatureIsProofTestIntegration(ColumnMappingView columnMapping) {
		return ExtendedFeature.valueOf(columnMapping.getRequiredExtendedFeature()) != ExtendedFeature.ProofTestIntegration;
	}

}
