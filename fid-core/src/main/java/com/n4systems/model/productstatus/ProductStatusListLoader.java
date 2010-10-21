package com.n4systems.model.productstatus;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductStatusListLoader extends ListLoader<AssetStatus> {

	public ProductStatusListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssetStatus> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, filter);
		builder.addOrder("name");
		
		List<AssetStatus> assetStatuses = builder.getResultList(em);
		return assetStatuses;
	}

}
