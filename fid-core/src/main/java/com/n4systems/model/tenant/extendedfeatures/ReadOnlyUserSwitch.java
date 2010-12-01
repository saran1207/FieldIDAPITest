package com.n4systems.model.tenant.extendedfeatures;

import java.util.Date;

import javax.persistence.Query;


import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.ReadOnlyUserIdListLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;

public class ReadOnlyUserSwitch extends ExtendedFeatureSwitch {
	
	public ReadOnlyUserSwitch(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.ReadOnlyUser);
	}

	@Override
	protected void featureSetup(Transaction transaction) {
	}
	
	@Override
	protected void featureTearDown(Transaction transaction) {
		deleteAllReadOnlyUsers(transaction);
	}
	
	private void deleteAllReadOnlyUsers(Transaction transaction) {
		ReadOnlyUserIdListLoader loader = new ReadOnlyUserIdListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant()));
		String updateQuery = "UPDATE " + User.class.getName() + " SET modified = :now, deleted = true WHERE id in (:ids)";
		Query query = transaction.getEntityManager().createQuery(updateQuery);
		query.setParameter("now", new Date());
		LargeInListQueryExecutor queryRunner = new LargeInListQueryExecutor();
		queryRunner.executeUpdate(query, loader.load());
	}

}
