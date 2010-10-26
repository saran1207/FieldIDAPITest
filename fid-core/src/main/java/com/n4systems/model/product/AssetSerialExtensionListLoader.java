package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.AssetSerialExtension;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetSerialExtensionListLoader extends ListLoader<AssetSerialExtension> {

	public AssetSerialExtensionListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssetSerialExtension> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetSerialExtension> builder = new QueryBuilder<AssetSerialExtension>(AssetSerialExtension.class, filter);
		builder.setOrder("extensionLabel");
		
		List<AssetSerialExtension> extensions = builder.getResultList(em);
		return extensions;
	}

}
