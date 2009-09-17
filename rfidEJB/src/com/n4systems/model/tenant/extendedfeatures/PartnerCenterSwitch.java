package com.n4systems.model.tenant.extendedfeatures;

import java.util.Date;

import javax.persistence.Query;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class PartnerCenterSwitch extends ExtendedFeatureSwitch {
	
	public PartnerCenterSwitch(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.PartnerCenter);
	}

	@Override
	protected void featureSetup(Transaction transaction) {
	}
	
	@Override
	protected void featureTearDown(Transaction transaction) {
		deleteAllCustomerUsers(transaction);
	}
	
	private void deleteAllCustomerUsers(Transaction transaction) {
		String updateQuery = "UPDATE " + UserBean.class.getName() + " u SET dateModified = :now, deleted=true WHERE owner.customerOrg IS NOT NULL";
		Query query = transaction.getEntityManager().createQuery(updateQuery);
		query.setParameter("now", new Date());
		query.executeUpdate();
	}

}
