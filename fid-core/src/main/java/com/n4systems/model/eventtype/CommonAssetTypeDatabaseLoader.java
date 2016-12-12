package com.n4systems.model.eventtype;

import com.n4systems.exceptions.IdListTooBigException;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import javax.persistence.EntityManager;
import java.util.List;

public class CommonAssetTypeDatabaseLoader extends ListLoader<AssetType> implements CommonAssetTypeLoader {


	private List<Long> assetIds;
	private final ConfigurationProvider configContext;

	public CommonAssetTypeDatabaseLoader(SecurityFilter filter, ConfigurationProvider configContext) {
		super(filter);
		this.configContext = configContext;
	}
	
	@Override
	protected List<AssetType> load(EntityManager em, SecurityFilter filter) {
		
		if (assetIds.size() > configContext.getLong(ConfigEntry.MASS_ACTIONS_LIMIT)) {
			throw new IdListTooBigException("the id list can not exceed [" + configContext.getLong(ConfigEntry.MASS_ACTIONS_LIMIT) + "]");
		}
		
		
		QueryBuilder<AssetType> builder = getAssetTypeQueryBuilder(filter);
		builder.addWhere(Comparator.IN, "assetIds", "id", assetIds);
		
		builder.addPostFetchPaths("eventTypes");
		
		builder.setSimpleSelect("type", true);
		return builder.getResultList(em);
	}
	
	protected QueryBuilder<AssetType> getAssetTypeQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<AssetType>(Asset.class, filter);
	}

	public CommonAssetTypeLoader forAssets(List<Long> assetIds) {
		this.assetIds = assetIds;
		return this;
	}

	

}
