package com.n4systems.fieldid.ws.v1.resources.asset;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

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
				QueryBuilder<SubAsset> query = new QueryBuilder<>(SubAsset.class);
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
					logger.warn("SubAsset already exists for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				}
			} else {
				logger.error("Failed to find Assets for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				throw new NotFoundException("SubAsset", masterAssetSid);
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
				QueryBuilder<SubAsset> query = new QueryBuilder<>(SubAsset.class);
				query.addWhere(WhereClauseFactory.create("masterAsset", masterAsset));
				query.addWhere(WhereClauseFactory.create("asset", asset));
				SubAsset subAsset = persistenceService.find(query);
				if(subAsset != null) {
					persistenceService.delete(subAsset);
					logger.info("SubAsset deleted for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				} else {
					logger.warn("Failed to find SubAset for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				}
			} else {
				logger.error("Failed to find Assets for masterAssetSid: " + masterAssetSid + " and assetSid: " + assetSid);
				throw new NotFoundException("SubAsset", masterAssetSid);
			}
		}
	}

    /**
     * In a situation where you can't determine the ID of the MasterAsset to a SubAsset, but you need to break that link
     * so you can link that SubAsset to a different Master Asset?  We have you covered!!
     * @param subAssetSid The SID of a subasset that you want to break out from the Master/Sub Asset Relationship.
     */
    @DELETE
    @Path("{subAssetSid}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Transactional
    public void breakSubAssetLink(@PathParam("subAssetSid") String subAssetSid) {
        logger.info("breakSubAssetLink for subAssetSid: " + subAssetSid);
        if(subAssetSid != null && !subAssetSid.isEmpty()) {
            Asset subAsset = assetService.findByMobileId(subAssetSid);

            if(subAsset != null) {
                QueryBuilder<SubAsset> query = new QueryBuilder<>(SubAsset.class);
                query.addWhere(WhereClauseFactory.create("asset", subAsset));
                SubAsset breakMe = persistenceService.find(query);

                if(breakMe != null) {
                    persistenceService.delete(breakMe);
                    logger.info("SubAsset deleted for SubAssetSid: " + subAssetSid);
                } else {
                    logger.warn("Failed to find SubAsset with SubAssetSid: " + subAssetSid);
                }
            } else {
                logger.error("Failed to find Asset for SubAssetId: " + subAssetSid);
                throw new NotFoundException("SubAsset", subAssetSid);
            }
        }
    }
	
	public ApiSubAsset findMasterAsset(Asset asset) {
		QueryBuilder<SubAsset> query = new QueryBuilder<>(SubAsset.class);
		query.addWhere(WhereClauseFactory.create("asset", asset));
		SubAsset subAsset = persistenceService.find(query); // Assumes that there will be only one MasterAsset for this one.
        return subAsset != null ? convertToApiSubAsset(subAsset.getMasterAsset()) : null;
	}
	
	public List<ApiSubAsset> findAndConvertSubAssets(Asset asset) {
		List<ApiSubAsset> apiSubAssets = new ArrayList<>();
		List<SubAsset> subAssets = findSubAssets(asset);
		
		for(SubAsset subAsset: subAssets) {
			ApiSubAsset apiSubAsset = convertToApiSubAsset(subAsset.getAsset());
			apiSubAssets.add(apiSubAsset);
		}
		
		return apiSubAssets;
	}
	
	public List<SubAsset> findSubAssets(Asset asset) {
		QueryBuilder<SubAsset> query = new QueryBuilder<>(SubAsset.class);
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
