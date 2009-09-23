package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.NonSecuredListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class OrgConnectionByTenantListLoader extends NonSecuredListLoader<OrgConnection> {

	private Long vendorTenantId;
	private Long customerTenantId;

	@Override
	protected List<OrgConnection> load(EntityManager em) {
		if (vendorTenantId == null && customerTenantId == null) {
			// we should not let this query return if one of the two is not
			// specified, lest we return all the connections in the system ... that would be bad
			throw new SecurityException("Either vendor or customer tenantId must be specified");
		}

		QueryBuilder<OrgConnection> builder = new QueryBuilder<OrgConnection>(OrgConnection.class);

		if (vendorTenantId != null) {
			builder.addSimpleWhere("vendor.tenant.id", vendorTenantId);
		}

		if (customerTenantId != null) {
			builder.addSimpleWhere("customer.tenant.id", customerTenantId);
		}

		List<OrgConnection> connections = builder.getResultList(em);
		return connections;
	}

	public OrgConnectionByTenantListLoader setVendorTenantId(Long vendorTenantId) {
		this.vendorTenantId = vendorTenantId;
		return this;
	}

	public OrgConnectionByTenantListLoader setCustomerTenantId(Long customerTenantId) {
		this.customerTenantId = customerTenantId;
		return this;
	}
}
