package com.n4systems.fieldid.ws.v1.resources.asset;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("subAsset")
public class ApiSubAssetResource extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(ApiSubAssetResource.class);
	@Autowired private AssetService assetService;
	
	@PUT
	@Path("{masterAssetSid}/{assetSid}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional
	public void attachSubAsset(@PathParam("masterAssetSid") String masterAssetSid, @PathParam("assetSid") String assetSid) {
		logger.info("attachSubAsset for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
		if(masterAssetSid != null && assetSid != null) {
			Asset masterAsset = assetService.findByMobileId(masterAssetSid);
			Asset asset = assetService.findByMobileId(assetSid);
			
			if(masterAsset != null && asset != null){
				QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class);
				query.addWhere(WhereClauseFactory.create("masterAsset", masterAsset));
				query.addWhere(WhereClauseFactory.create("asset", asset));
				if(!persistenceService.exists(query)) {
					SubAsset subAsset = new SubAsset();
					subAsset.setMasterAsset(masterAsset);
					subAsset.setAsset(asset);
					subAsset.setWeight(1L);
					persistenceService.save(subAsset);
					logger.info("SubAsset created for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				} else {
					logger.error("SubAsset already exists for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				}
			} else {
				logger.error("Failed to find Assets for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
			}
		}	
	}
	
	@DELETE
	@Path("{masterAssetSid}/{assetSid}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional
	public void detachSubAsset(@PathParam("masterAssetSid") String masterAssetSid, @PathParam("assetSid") String assetSid) {
		logger.info("detachSubAsset for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
		if(masterAssetSid != null && assetSid != null) {
			Asset masterAsset = assetService.findByMobileId(masterAssetSid);
			Asset asset = assetService.findByMobileId(assetSid);
			
			if(masterAsset != null && asset != null) {
				QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class);
				query.addWhere(WhereClauseFactory.create("masterAsset", masterAsset));
				query.addWhere(WhereClauseFactory.create("asset", asset));
				SubAsset subAsset = persistenceService.find(query);
				if(subAsset != null) {
					persistenceService.delete(subAsset);
					logger.info("SubAsset deleted for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				} else {
					logger.error("Failed to find SubAset for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				}
			} else {
				logger.error("Failed to find Assets for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
			}
		}
	}
	
	public ApiSubAsset findMasterAsset(Asset asset) {
		QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class);
		query.addWhere(WhereClauseFactory.create("asset", asset));
		SubAsset subAsset = persistenceService.find(query); // Assumes that there will be only one MasterAsset for this one.
		ApiSubAsset apiSubAsset = subAsset != null ? convertToApiSubAsset(subAsset.getMasterAsset()) : null;
		return apiSubAsset;
	}
	
	public List<ApiSubAsset> findSubAssets(Asset asset) {
		List<ApiSubAsset> apiSubAssets = new ArrayList<ApiSubAsset>();		
		QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class);
		query.addWhere(WhereClauseFactory.create("masterAsset", asset));
		List<SubAsset> subAssets = persistenceService.findAll(query);
		
		for(SubAsset subAsset: subAssets) {
			ApiSubAsset apiSubAsset = convertToApiSubAsset(subAsset.getAsset());
			apiSubAssets.add(apiSubAsset);
		}
		
		return apiSubAssets;
	}
	
	private ApiSubAsset convertToApiSubAsset(Asset asset) {
		ApiSubAsset apiSubAsset = new ApiSubAsset();
		apiSubAsset.setSid(asset.getMobileGUID());
		apiSubAsset.setType(asset.getType().getName());
		apiSubAsset.setIdentifier(asset.getIdentifier());
		return apiSubAsset;
	}
}
