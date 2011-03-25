package com.n4systems.export;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;

public class SubAssetsForMasterLoader {
	private final EntityManager em;
	
	public SubAssetsForMasterLoader(EntityManager em) {
		this.em = em;
	}
	
	public List<SubAsset> loadSubAssets(Asset master) {
		String jpql = String.format("FROM %s s WHERE s.masterAsset.id = :masterId ORDER BY s.weight", SubAsset.class.getName());
		TypedQuery<SubAsset> query = em.createQuery(jpql, SubAsset.class);
		query.setParameter("masterId", master.getId());

		List<SubAsset> subAssets = query.getResultList();
		return subAssets;
	}
}
