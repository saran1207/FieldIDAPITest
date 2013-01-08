package com.n4systems.fieldid.ws.v1.resources.asset;

import com.n4systems.fieldid.service.StorePlatformContext;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedEventResource;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.SmartSearchWhereClause;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.asset.AssetSaveServiceSpring;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

@Component
@Path("asset")
public class ApiAssetResource extends ApiResource<ApiAsset, Asset> {
	private static Logger logger = Logger.getLogger(ApiAssetResource.class);
	
	@Autowired private AssetService assetService;	
	@Autowired private AssetSaveServiceSpring assetSaveService;
	@Autowired private ApiEventHistoryResource apiEventHistoryResource;
	@Autowired private ApiEventScheduleResource apiEventScheduleResource;
	@Autowired private ApiAssetAttachmentResource apiAttachmentResource;
	@Autowired private ApiSubAssetResource apiSubAssetResource;
	@Autowired private ApiSavedEventResource apiSavedEventResource;
	
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAsset> findAll(
			@QueryParam("after") DateParam after,
			@QueryParam("searchText") String searchText,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize,
			@QueryParam("owner") Long ownerId,
			@DefaultValue("false") @QueryParam("downloadEvents") boolean downloadEvents) {
		
		QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
		builder.addOrder("created");
		
		if (ownerId != null) {
			builder.applyFilter(new OwnerAndDownFilter(findEntity(BaseOrg.class, ownerId)));
		}
		
		if (searchText != null) {
			builder.addWhere(new SmartSearchWhereClause(searchText, true, true, true));
		}

		List<Asset> assets = persistenceService.findAll(builder, page, pageSize);
		Long total = persistenceService.count(builder);
		
		List<ApiAsset> apiAssets = convertAllAssetsToApiModels(assets, downloadEvents, SyncDuration.ALL);
		ListResponse<ApiAsset> response = new ListResponse<ApiAsset>(apiAssets, page, pageSize, total);
		return response;
	}
	
	@GET
	@Path("list")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAsset> findAll(@QueryParam("id") List<String> assetIds, 
			@DefaultValue("false") @QueryParam("downloadEvents") boolean downloadEvents,
			@DefaultValue("YEAR") @QueryParam("syncDuration") SyncDuration syncDuration) {
		QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileGUID", assetIds));
		
		List<Asset> assets = persistenceService.findAll(builder);
		
		List<ApiAsset> apiAssets = convertAllAssetsToApiModels(assets, downloadEvents, syncDuration);
		ListResponse<ApiAsset> response = new ListResponse<ApiAsset>(apiAssets, 0, assets.size(), assets.size());
		return response;
	}
	
	@GET
	@Path("{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ApiAsset find(@PathParam("id") String id,
			@DefaultValue("false") @QueryParam("downloadEvents") boolean downloadEvents) {
		Asset asset = assetService.findByMobileId(id);
		if (asset == null) {
			throw new NotFoundException("Asset", id);
		}
		
		ApiAsset apiModel = convertToApiAsset(asset, downloadEvents, SyncDuration.ALL);
		return apiModel;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
    @StorePlatformContext
	public void saveAsset(ApiAsset apiAsset) {
		Asset asset;
		Asset existingAsset = assetService.findByMobileId(apiAsset.getSid());
		
		// Save Asset.
		if(existingAsset == null) {
			logger.info("Creating Asset " + apiAsset.getIdentifier());
			asset = createAsset(apiAsset);
		} else {
			logger.info("Updating Asset " + apiAsset.getIdentifier());
			asset = updateAsset(apiAsset, existingAsset);
		}

		logger.info("Saved Asset " + asset.getIdentifier());
	}

	@PUT
	@Path("multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
    @StorePlatformContext
	public void multiAddAsset(ApiMultiAddAsset multiAddAsset) {
		ApiAsset assetTemplate = multiAddAsset.getAssetTemplate();
		for (ApiAssetIdentifiers identifiers: multiAddAsset.getIdentifiers()) {
			assetTemplate.setSid(identifiers.getSid());
			assetTemplate.setIdentifier(identifiers.getIdentifier());
			assetTemplate.setRfidNumber(identifiers.getRfidNumber());
			assetTemplate.setCustomerRefNumber(identifiers.getCustomerRefNumber());

			logger.info("Creating Asset " + assetTemplate.getIdentifier());
			createAsset(assetTemplate);
		}

		logger.info("Saved " + multiAddAsset.getIdentifiers().size() + " Assets ");
	}

	private Asset createAsset(ApiAsset apiAsset) {
		Asset asset = new Asset();
		converApiAsset(apiAsset, asset);
		List<AssetAttachment> uploadedAttachments = apiAttachmentResource.convertApiListToEntityList(apiAsset.getAttachments(), asset);		
		return assetSaveService.create(asset, uploadedAttachments, apiAsset.getImage());
	}
	
	private Asset updateAsset(ApiAsset apiAsset, Asset asset) {
		converApiAsset(apiAsset, asset);		
		List<AssetAttachment> uploadedAttachments = apiAttachmentResource.convertApiListToEntityList(apiAsset.getAttachments(), asset);		
		List<AssetAttachment> existingAttachments = apiAttachmentResource.findAllAssetAttachments(asset.getMobileGUID());
		asset.setSubAssets(apiSubAssetResource.findSubAssets(asset)); // So they don't get cleared! - WEB-3031
		return assetSaveService.update(asset, existingAttachments, uploadedAttachments, apiAsset.getImage());
	}
	
	protected List<ApiAsset> convertAllAssetsToApiModels(List<Asset> assets, boolean downloadEvents, SyncDuration syncDuration) {
		List<ApiAsset> apiAssets = new ArrayList<ApiAsset>();
		for (Asset asset: assets) {
			apiAssets.add(convertToApiAsset(asset, downloadEvents, syncDuration));
		}
		return apiAssets;
	}
	
	protected ApiAsset convertToApiAsset(Asset asset, boolean downloadEvents, SyncDuration syncDuration) {
		ApiAsset apiAsset = convertEntityToApiModel(asset);		
		apiAsset.setSchedules(apiEventScheduleResource.findAllSchedules(asset.getId(), syncDuration));
		if(downloadEvents) {
			apiAsset.setEvents(apiSavedEventResource.findLastEventOfEachType(asset.getId()));
		}		
		return apiAsset;
	}

	@Override
	protected ApiAsset convertEntityToApiModel(Asset asset) {
		ApiAsset apiAsset = new ApiAsset();
		apiAsset.setSid(asset.getMobileGUID());
		apiAsset.setModified(asset.getModified());
		apiAsset.setActive(asset.isActive());
		apiAsset.setOwnerId(asset.getOwner().getId());
		apiAsset.setIdentifier(asset.getIdentifier());
		apiAsset.setRfidNumber(asset.getRfidNumber());
		apiAsset.setCustomerRefNumber(asset.getCustomerRefNumber());
		apiAsset.setPurchaseOrder(asset.getPurchaseOrder());
		apiAsset.setComments(asset.getComments());
		apiAsset.setIdentified(asset.getIdentified());
		apiAsset.setLastEventDate(asset.getLastEventDate());
		apiAsset.setTypeId(asset.getType().getId());
		apiAsset.setNonIntergrationOrderNumber(asset.getNonIntergrationOrderNumber());
		apiAsset.setImage(loadAssetImage(asset));
		apiAsset.setMasterAsset(apiSubAssetResource.findMasterAsset(asset));
		apiAsset.setSubAssets(apiSubAssetResource.findAndConvertSubAssets(asset));
		
		if (asset.getAssetStatus() != null) {
			apiAsset.setAssetStatusId(asset.getAssetStatus().getId());
		}
		
		if (asset.getIdentifiedBy() != null) {
			apiAsset.setIdentifiedById(asset.getIdentifiedBy().getId());	
		}
		
		if (asset.getAssignedUser() != null) {
			apiAsset.setAssignedUserId(asset.getAssignedUser().getId());
		}
		
		if (asset.getGpsLocation() != null) {
			if (asset.getGpsLocation().getLatitude() != null) {
				apiAsset.setGpsLatitude(asset.getGpsLocation().getLatitude());
			}
			
			if (asset.getGpsLocation().getLongitude() != null) {
				apiAsset.setGpsLongitude(asset.getGpsLocation().getLongitude());
			}
		}
		
		if (asset.getAdvancedLocation() != null) {
			apiAsset.setFreeformLocation(asset.getAdvancedLocation().getFreeformLocation());
			
			if (asset.getAdvancedLocation().getPredefinedLocation() != null) {
				apiAsset.setPredefinedLocationId(asset.getAdvancedLocation().getPredefinedLocation().getId());
			}
		}		
		
		apiAsset.setAttributeValues(findAllAttributeValues(asset));		
		apiAsset.setEventHistory(apiEventHistoryResource.findAllEventHistory(asset.getMobileGUID()));
		apiAsset.setAttachments(apiAttachmentResource.findAllAttachments(asset.getMobileGUID()));			
		
		return apiAsset;
	}
	
	private void converApiAsset(ApiAsset apiAsset, Asset asset) {
		asset.setTenant(getCurrentTenant());
		asset.setMobileGUID(apiAsset.getSid());

		asset.setType(findEntity(AssetType.class, apiAsset.getTypeId()));
		asset.setOwner(findEntity(BaseOrg.class, apiAsset.getOwnerId()));
		asset.setIdentifier(apiAsset.getIdentifier());
		asset.setRfidNumber(apiAsset.getRfidNumber());
		asset.setCustomerRefNumber(apiAsset.getCustomerRefNumber());
		asset.setPurchaseOrder(apiAsset.getPurchaseOrder());
		asset.setComments(apiAsset.getComments());
		asset.setIdentified(apiAsset.getIdentified());
		asset.setNonIntergrationOrderNumber(apiAsset.getNonIntergrationOrderNumber());
		asset.setModified(apiAsset.getModified());
		
		if(apiAsset.getAssetStatusId() > 0) {
			asset.setAssetStatus(findEntity(AssetStatus.class, apiAsset.getAssetStatusId()));
		} else {
			asset.setAssetStatus(null);
		}
		
		if(apiAsset.getIdentifiedById() > 0) {
			asset.setIdentifiedBy(findEntity(User.class, apiAsset.getIdentifiedById()));
		}
		
		if(apiAsset.getAssignedUserId() > 0) {
			asset.setAssignedUser(findEntity(User.class, apiAsset.getAssignedUserId()));
		}
		
		if(apiAsset.getGpsLatitude() != null && apiAsset.getGpsLongitude() != null) {
			GpsLocation gpsLocation = new GpsLocation(apiAsset.getGpsLatitude(), apiAsset.getGpsLongitude());
			
			if(gpsLocation.isValid()) {
				asset.setGpsLocation(gpsLocation);
			}
		}
		
		if(apiAsset.getFreeformLocation() != null || (apiAsset.getPredefinedLocationId() != null &&  apiAsset.getPredefinedLocationId() > 0)) {
			Location location = new Location();
			location.setFreeformLocation(apiAsset.getFreeformLocation());
			
			if(apiAsset.getPredefinedLocationId() != null && apiAsset.getPredefinedLocationId()  > 0) {
				location.setPredefinedLocation(findEntity(PredefinedLocation.class, apiAsset.getPredefinedLocationId()));
			}
			
			asset.setAdvancedLocation(location);
		}
		
		asset.setInfoOptions(convertAttributeValues(apiAsset.getAttributeValues(), asset));
	}



    private List<ApiAttributeValue>  findAllAttributeValues(Asset asset) {
		List<ApiAttributeValue> apiAttributeValues = new ArrayList<ApiAttributeValue>();
		
		for (InfoOptionBean option: asset.getInfoOptions()) {
			apiAttributeValues.add(convertInfoOption(option));
		}
		
		return apiAttributeValues;
	}
	
	private Set<InfoOptionBean> convertAttributeValues(List<ApiAttributeValue> apiAttributeValues, Asset asset) {
		Set<InfoOptionBean> infoOptions = new TreeSet<InfoOptionBean>();
		
		for(ApiAttributeValue apiAttributeValue : apiAttributeValues) {
			infoOptions.add(convertApiAttributeValue(apiAttributeValue, asset));
		}
		
		return infoOptions;
	}
	
	private ApiAttributeValue convertInfoOption(InfoOptionBean infoOption) {
		ApiAttributeValue attribValue = new ApiAttributeValue();
		attribValue.setAttributeId(infoOption.getInfoField().getUniqueID());
		
		if (infoOption.getInfoField().isDateField()) {
			if (infoOption.getName() != null) {
				attribValue.setValue(new Date(Long.parseLong(infoOption.getName())));
			}
		} else {
			attribValue.setValue(infoOption.getName());
		}
		return attribValue;
	}
	
	// Convert a given attributevalue into InfoOptionBean of date, static(select, combobox) or text type.
	// During Edit, because we are not storing InfoOptionId in client side, we have to look up by infoFieldID for a given asset.
	private InfoOptionBean convertApiAttributeValue(ApiAttributeValue apiAttributeValue, Asset asset) {
		Long infoFieldID = apiAttributeValue.getAttributeId();
		Object value = apiAttributeValue.getValue();
		InfoFieldBean infoField;
		
		// Get the infoOption from the existing assetInfoOptions if we have a match.
		InfoOptionBean infoOptionBean = findInfoOption(infoFieldID, asset);
		
		if(infoOptionBean != null) {
			infoField = infoOptionBean.getInfoField();
		} else {
			infoField = persistenceService.findNonSecure(InfoFieldBean.class, infoFieldID);
			infoOptionBean = new InfoOptionBean();
			infoOptionBean.setInfoField(infoField);
		}		
		
		if(value instanceof Date) {
			infoOptionBean.setName(Long.toString(((Date) value).getTime()));
		} else {
			InfoOptionBean staticOption = findStaticInfoOption(infoField, value);
			
			if(staticOption != null) {
				//TODO, We need to remove the existing infoOptionBean if there was one at this point! Ask Mark.
				infoOptionBean = staticOption;
			} else {
				infoOptionBean.setName(value.toString());
			}
		}
		
		return infoOptionBean;
	}
	
	// For the given InfoFieldId, check if we have an InfoOption in the asset.infoOptions.
	private InfoOptionBean findInfoOption(Long infoFieldId, Asset asset) {
		for (InfoOptionBean infoOption : asset.getInfoOptions()) {
			if(infoOption.getInfoField().getUniqueID().equals(infoFieldId)) {
				return infoOption;
			}
		}
		
		return null;
	}
	
	// For the given InfoField, check if we have a static InfoOption for the given value.
	private InfoOptionBean findStaticInfoOption(InfoFieldBean infoField, Object value) {
		Set<InfoOptionBean> staticOptions = infoField.getUnfilteredInfoOptions();
		for (InfoOptionBean staticOption: staticOptions) {
			if (staticOption.getName().equals(value)) {
				return staticOption;
			}
		}
		
		return null;
	}
	
	private byte[] loadAssetImage(Asset asset) {
		byte[] image = null;		
		if(asset.getImageName() != null) {
			S3Service s3Service = ServiceLocator.getS3Service();
			try {
				image = s3Service.downloadAssetProfileMediumImage(asset.getId(), asset.getImageName());
			} catch (IOException ex) {
				logger.warn("Unable to load asset image for asset: " + asset.getIdentifier(), ex);
			}
		}		
		return image;
	}	
}
