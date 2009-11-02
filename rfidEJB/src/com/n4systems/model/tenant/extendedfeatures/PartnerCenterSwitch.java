package com.n4systems.model.tenant.extendedfeatures;

import java.util.Date;

import javax.persistence.Query;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.CustomerUserIdListLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;

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
		CustomerUserIdListLoader loader = new CustomerUserIdListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant()));
		String updateQuery = "UPDATE " + UserBean.class.getName() + " SET dateModified = :now, deleted=true WHERE id in (:ids)";
		Query query = transaction.getEntityManager().createQuery(updateQuery);
		query.setParameter("now", new Date());
		LargeInListQueryExecutor queryRunner = new LargeInListQueryExecutor();
		queryRunner.executeUpdate(query, loader.load());
	}

}
