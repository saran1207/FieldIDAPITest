package com.n4systems.model.asset;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SmartSearchLoader extends ListLoader<Asset> {

	private String searchText;
	private boolean useIdentifier = true;
	private boolean useRfidNumber = true;
	private boolean useRefNumber = true;
    private Integer maxResults = null;
	
	public SmartSearchLoader(SecurityFilter filter) {
		super(filter);
	}

	protected QueryBuilder<Asset> createQuery(SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, filter);
		builder.addWhere(new SmartSearchWhereClause(searchText, useIdentifier, useRfidNumber, useRefNumber));
		builder.addOrder("created");
		
		return builder;
	}

	public void setUseIdentifier(boolean useIdentifier) {
		this.useIdentifier = useIdentifier;
	}

	public void setUseRfidNumber(boolean useRfidNumber) {
		this.useRfidNumber = useRfidNumber;
	}

	public void setUseRefNumber(boolean useRefNumber) {
		this.useRefNumber = useRefNumber;
	}

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }


	@Override
	public List<Asset> load(EntityManager em, SecurityFilter filter) {
        List<Asset> assets;
        if (maxResults != null) {
            assets = createQuery(filter).getResultList(em, 0, maxResults);
        } else {
            assets = createQuery(filter).getResultList(em);
        }

		return assets;
	}

	public SmartSearchLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
}
