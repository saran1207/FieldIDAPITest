package com.n4systems.export.converters;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class MasterAssetConverter extends AssetConverter {
	
	public MasterAssetConverter(EntityManager em) {
		super(em);
	}
	
	@Override
	protected void marshalEntity(Asset asset, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalEntity(asset, writer, context);
		
		List<SubAsset> subAssets = loadSubAssets(asset);
		System.out.println("\tFound " + subAssets.size() + " sub assets");
		writeIterable(writer, context, "Components", subAssets, new SubAssetConverter(em));
	}

	protected List<SubAsset> loadSubAssets(Asset master) {
		String jpql = String.format("FROM %s s WHERE s.masterAsset.id = :masterId ORDER BY s.weight", SubAsset.class.getName());
		TypedQuery<SubAsset> query = em.createQuery(jpql, SubAsset.class);
		query.setParameter("masterId", master.getId());

		List<SubAsset> subAssets = query.getResultList();
		return subAssets;
	}
}
