package com.n4systems.model.asset;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.AssetExtension;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetExtensionListLoader extends ListLoader<AssetExtension> {

	public AssetExtensionListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssetExtension> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetExtension> builder = new QueryBuilder<AssetExtension>(AssetExtension.class, filter);
		builder.setOrder("extensionLabel");
		
		List<AssetExtension> extensions = builder.getResultList(em);
		return extensions;
	}

}
