package com.n4systems.fieldid.actions;



import java.util.Date;

import com.n4systems.compliance.ComplianceCheck;
import com.n4systems.ejb.ComplianceManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.util.DateHelper;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Compliance)
public class ComplianceAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ComplianceCheck complianceCheck;
	
	private ComplianceManager complianceManager;

	
	
	public ComplianceAction(ComplianceManager complianceManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.complianceManager = complianceManager;
	}


	public ComplianceCheck getComplianceCheck() {
		return complianceCheck;
	}
	
	
	public String doShow() {
			
		complianceCheck = complianceManager.multiAssetComplianceCheck( getTenant() );
	
		
		return SUCCESS;
	}
	
	public Date getYesterday() {
		return DateHelper.getYesterday();
	}
}
