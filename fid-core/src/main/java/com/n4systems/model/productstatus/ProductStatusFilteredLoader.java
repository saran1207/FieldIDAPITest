package com.n4systems.model.productstatus;

import javax.persistence.EntityManager;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductStatusFilteredLoader extends SecurityFilteredLoader<AssetStatus> {
	private Long id;

	public ProductStatusFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AssetStatus load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, filter);
		builder.addSimpleWhere("uniqueID", id);
		
		AssetStatus assetStatus = builder.getSingleResult(em);
	    return assetStatus;
	}

	public ProductStatusFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
