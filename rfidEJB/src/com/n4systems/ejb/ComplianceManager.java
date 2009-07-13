package com.n4systems.ejb;

import javax.ejb.Local;

import com.n4systems.compliance.ComplianceCheck;
import com.n4systems.model.Organization;

@Local
public interface ComplianceManager {
	
	
	public ComplianceCheck multiAssetComplianceCheck( Organization tenant );

}
