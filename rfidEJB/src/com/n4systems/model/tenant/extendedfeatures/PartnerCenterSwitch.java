package com.n4systems.model.tenant.extendedfeatures;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;

public class PartnerCenterSwitch extends ExtendedFeatureSwitch {

	
	
	public PartnerCenterSwitch(TenantOrganization tenant, PersistenceManager persistenceManager) {
		super(tenant, persistenceManager, ExtendedFeature.PartnerCenter);
	}


	@Override
	protected void featureSetup() {
	}
	
	@Override
	protected void featureTearDown() {
		deleteAllCustomerUsers();
	}
	
	
	private void deleteAllCustomerUsers() {
		String deleteCustomerUserSql = "UPDATE " + UserBean.class.getName() + " u SET dateModified = :now, deleted=true WHERE r_EndUser IS NOT NULL";
		Map<String,Object> updateValues = new HashMap<String, Object>();
		updateValues.put("now", new Date());
		
		persistenceManager.executeUpdate(deleteCustomerUserSql, updateValues);
	}

}
