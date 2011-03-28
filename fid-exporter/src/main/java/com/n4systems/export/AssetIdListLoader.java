package com.n4systems.export;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.persistence.loaders.Loader;

public class AssetIdListLoader extends Loader<List<IdWrapper>> {
	private final long tenantId;
	
	public AssetIdListLoader(long tenantId) {
		this.tenantId = tenantId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<IdWrapper> load(EntityManager em) {
		String jpql = String.format("SELECT new com.n4systems.export.IdWrapper(a.id) FROM %s a WHERE a.tenant.id = :tenantId AND a.id NOT IN (SELECT s.asset.id FROM %s s)", Asset.class.getName(), SubAsset.class.getName());
		Query query = em.createQuery(jpql);
		query.setParameter("tenantId", tenantId);

		System.out.println("Loading asset ids... ");
		List<IdWrapper> assets = query.getResultList();
		
		System.out.println(String.format("Found: %d Master Assets", assets.size()));
		return assets;
	}
	
}
