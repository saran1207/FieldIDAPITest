package com.n4systems.model.safetynetwork;

import com.n4systems.model.Asset;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectInClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import java.util.List;

import javax.persistence.EntityManager;

public class ProductsByNetworkIdLoader extends ListLoader<Asset> {

	private Long networkId;
	private Long excludeProductId;
	private boolean bypassSecurityEnhancement = false;
	
	public ProductsByNetworkIdLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	public List<Asset> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<Tenant> connectedTenantsQuery = new QueryBuilder<Tenant>(TypedOrgConnection.class, filter);
		connectedTenantsQuery.setSimpleSelect("connectedOrg.tenant", true);

        SubSelectInClause insideSafetyNetworkSubClause = new SubSelectInClause("owner.tenant", connectedTenantsQuery);

		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("networkId", networkId));
		builder.addPostFetchPaths(Asset.POST_FETCH_ALL_PATHS);
        builder.addWhere(insideSafetyNetworkSubClause);
		
		if (excludeProductId != null) {
			builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", excludeProductId));
		}

		builder.addPostFetchPaths("infoOptions");
		
		List<Asset> unsecuredProducts = builder.getResultList(em);
		
		if (bypassSecurityEnhancement) {
			return unsecuredProducts;
		}
		
		PersistenceManager.setSessionReadOnly(em);
		
		List<Asset> enhancedAssets = EntitySecurityEnhancer.enhanceList(unsecuredProducts, filter);
		
		return enhancedAssets;
	}

	public ProductsByNetworkIdLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}

	public ProductsByNetworkIdLoader setExcludeProductId(Long excludeProductId) {
		this.excludeProductId = excludeProductId;
		return this;
	}
	
	public ProductsByNetworkIdLoader bypassSecurityEnhancement() {
		bypassSecurityEnhancement = true;
		return this;
	}
}
