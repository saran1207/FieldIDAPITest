package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.product.SmartSearchLoader;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class SafetyNetworkSmartSearchLoader extends ListLoader<Product> {
	private final VendorLinkedOrgLoader linkedOrgLoader;
	private String searchText;
	private boolean useSerialNumber = true;
	private boolean useRfidNumber = true;
	private boolean useRefNumber = true;
	
	public SafetyNetworkSmartSearchLoader(SecurityFilter filter, VendorLinkedOrgLoader linkedOrgLoader) {
		super(filter);
		this.linkedOrgLoader = linkedOrgLoader;
	}
	
	public SafetyNetworkSmartSearchLoader(SecurityFilter filter) {
		this(filter, new VendorLinkedOrgLoader(filter));
	}

	protected SmartSearchLoader createSmartSearchListLoader() {
		// we use a null security filter here since one will be passed in directly, when we run the load()
		SmartSearchLoader smartSearchLoader = new SmartSearchLoader(null) {
			@Override
			protected QueryBuilder<Product> createQuery(SecurityFilter filter) {
				// we need to add the published clause to this query so we'll override the createQuery method and add it in there
				return super.createQuery(filter).addWhere(WhereClauseFactory.create("published", true));
			}
		};
		
		smartSearchLoader.setUseSerialNumber(useSerialNumber);
		smartSearchLoader.setUseRfidNumber(useRfidNumber);
		smartSearchLoader.setUseRefNumber(useRefNumber);
		
		return smartSearchLoader;
	}
	
	@Override
	protected List<Product> load(EntityManager em, SecurityFilter filter) {
		InternalOrg vendorOrg = linkedOrgLoader.load(em, filter);
		
		SmartSearchLoader smartSearchLoader = createSmartSearchListLoader();
		smartSearchLoader.setSearchText(searchText);
		
		// since we were alowed to load this vendor org, the security on the next query will be done as is by them
		List<Product> product = smartSearchLoader.load(em, new OrgOnlySecurityFilter(vendorOrg));
		return product;
	}

	public SafetyNetworkSmartSearchLoader setVendorOrgId(Long vendorOrgId) {
		linkedOrgLoader.setLinkedOrgId(vendorOrgId);
		return this;
	}
	
	public SafetyNetworkSmartSearchLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
	public SafetyNetworkSmartSearchLoader useOnlySerialNumber() {
		setUseSerialNumber(true);
		setUseRfidNumber(false);
		setUseRefNumber(false);
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlyRfidNumber() {
		setUseRfidNumber(true);
		setUseSerialNumber(false);
		setUseRefNumber(false);
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlyRefNumber() {
		setUseRefNumber(true);
		setUseRfidNumber(false);
		setUseSerialNumber(false);
		return this;
	}

	public void setUseSerialNumber(boolean useSerialNumber) {
		this.useSerialNumber = useSerialNumber;
	}

	public void setUseRfidNumber(boolean useRfidNumber) {
		this.useRfidNumber = useRfidNumber;
	}

	public void setUseRefNumber(boolean useRefNumber) {
		this.useRefNumber = useRefNumber;
	}
}
