package com.n4systems.model.tenant.extendedfeatures;

import java.util.Date;

import javax.persistence.Query;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public class PartnerCenterSwitch extends ExtendedFeatureSwitch {
	
	public PartnerCenterSwitch(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.PartnerCenter);
	}

	@Override
	protected void featureSetup() {
	}
	
	@Override
	protected void featureTearDown() {
		deleteAllCustomerUsers();
	}
	
	private void deleteAllCustomerUsers() {
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
			
			Query query = transaction.getEntityManager().createQuery("UPDATE " + UserBean.class.getName() + " u SET dateModified = :now, deleted=true WHERE r_EndUser IS NOT NULL");
			query.setParameter("now", new Date());
			query.executeUpdate();
			
		} finally {
			PersistenceManager.finishTransaction(transaction);
		}
	}

}
