package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class OrgConnectionExistsLoader extends Loader<Boolean> {

	
	private Long customerId;
	private Long vendorId;

	@Override
	protected Boolean load(EntityManager em) {
		QueryBuilder<OrgConnection> query = new QueryBuilder<OrgConnection>(OrgConnection.class, new OpenSecurityFilter());

		query.addWhere(WhereClauseFactory.create("customer.id", customerId));
		query.addWhere(WhereClauseFactory.create("vendor.id", vendorId));
		
		return query.entityExists(em);
	}

	public OrgConnectionExistsLoader setCustomerId(Long customerId) {
		this.customerId = customerId;
		return this;
	}

	public OrgConnectionExistsLoader setVendorId(Long vendorId) {
		this.vendorId = vendorId;
		return this;
	}

}
