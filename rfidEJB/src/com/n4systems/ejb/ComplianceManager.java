package com.n4systems.ejb;

import javax.ejb.Local;

import com.n4systems.compliance.ComplianceCheck;
import com.n4systems.model.Tenant;

@Local
public interface ComplianceManager {
	
	
	public ComplianceCheck multiAssetComplianceCheck( Tenant tenant );

}
