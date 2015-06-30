package com.n4systems.fieldid.ws.v2.resources.asset;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApiSubAssetResource extends FieldIdPersistenceService {
	
	public ApiSubAsset findMasterAsset(Asset asset) {
		QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class);
		query.addWhere(WhereClauseFactory.create("asset", asset));
		SubAsset subAsset = persistenceService.find(query); // Assumes that there will be only one MasterAsset for this one.
		ApiSubAsset apiSubAsset = subAsset != null ? convertToApiSubAsset(subAsset.getMasterAsset()) : null;
		return apiSubAsset;
	}
	
	public List<ApiSubAsset> findAndConvertSubAssets(Asset asset) {
		List<ApiSubAsset> apiSubAssets = new ArrayList<ApiSubAsset>();		
		List<SubAsset> subAssets = findSubAssets(asset);
		
		for(SubAsset subAsset: subAssets) {
			ApiSubAsset apiSubAsset = convertToApiSubAsset(subAsset.getAsset());
			apiSubAssets.add(apiSubAsset);
		}
		
		return apiSubAssets;
	}
	
	public List<SubAsset> findSubAssets(Asset asset) {
		QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class);
		query.addWhere(WhereClauseFactory.create("masterAsset", asset));
		return persistenceService.findAll(query);
	}
	
	private ApiSubAsset convertToApiSubAsset(Asset asset) {
		ApiSubAsset apiSubAsset = new ApiSubAsset();
		apiSubAsset.setSid(asset.getMobileGUID());
		apiSubAsset.setType(asset.getType().getName());
		apiSubAsset.setIdentifier(asset.getIdentifier());
		return apiSubAsset;
	}
}
