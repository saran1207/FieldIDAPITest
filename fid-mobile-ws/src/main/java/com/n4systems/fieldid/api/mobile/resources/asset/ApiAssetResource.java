package com.n4systems.fieldid.api.mobile.resources.asset;

import com.n4systems.fieldid.api.mobile.resources.ApiModelHeader;
import com.n4systems.fieldid.api.mobile.resources.ApiResource;
import com.n4systems.fieldid.api.mobile.resources.assetattachment.ApiAssetAttachment;
import com.n4systems.fieldid.api.mobile.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.api.mobile.resources.assettype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.api.mobile.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.fieldid.api.mobile.resources.procedure.ApiProcedureDefinitionResourceV2;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.asset.AssetSaveServiceSpring;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoOptionBean;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("asset")
public class ApiAssetResource extends ApiResource<ApiAsset, Asset> {
	private static Logger logger = Logger.getLogger(ApiAssetResource.class);
	
	@Autowired private AssetService assetService;	
	@Autowired private AssetSaveServiceSpring assetSaveService;
	@Autowired private ApiAssetAttachmentResource apiAttachmentResource;
	@Autowired private ApiSubAssetResource apiSubAssetResource;
	@Autowired private ApiAttributeValueResource apiAttributeValueResource;
    @Autowired private LastEventDateService lastEventDateService;
    @Autowired private ApiProcedureDefinitionResourceV2 procedureDefinitionResource;
	@Autowired private S3Service s3Service;
	@Autowired private AssetSearchService assetSearchService;

	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryAssets(
			@QueryParam("rfidNumber") String rfidNumber,
			@QueryParam("identifier") String identifier,
			@QueryParam("referenceNumber") String referenceNumber,
			@QueryParam("assignedTo") Long assignedTo,
			@QueryParam("owner") Long owner,
			@QueryParam("location") String location,
			@QueryParam("predefinedLocationId") Long predefinedLocationId,
			@QueryParam("orderNumber") String orderNumber,
			@QueryParam("purchaseOrder") String purchaseOrder,
			@QueryParam("assetStatus") Long assetStatus,
			@QueryParam("assetType") Long assetType,
			@QueryParam("assetTypeGroup") Long assetTypeGroup,
			@QueryParam("identifiedFrom") Date identifiedFrom,
			@QueryParam("identifiedTo") Date identifiedTo,
			@QueryParam("orderByField") String orderByField,
			@QueryParam("orderByDirection") String orderByDirection) {


		AssetSearchCriteria searchCriteria = new AssetSearchCriteria();

		searchCriteria.setRfidNumber(rfidNumber);
		searchCriteria.setIdentifier(identifier);
		searchCriteria.setReferenceNumber(referenceNumber);
		searchCriteria.setOrderNumber(orderNumber);
		searchCriteria.setPurchaseOrder(purchaseOrder);

		if (assignedTo != null) {
			searchCriteria.setAssignedTo(persistenceService.find(User.class, assignedTo));
		}

		if (owner != null) {
			searchCriteria.setOwner(persistenceService.find(BaseOrg.class, owner));
		}

		PredefinedLocation predefinedLocation = null;
		if(predefinedLocationId != null && predefinedLocationId > 0) {
			predefinedLocation = persistenceService.find(PredefinedLocation.class, predefinedLocationId);
		}

		if(location != null || predefinedLocation != null) {
			searchCriteria.setLocation(new Location(predefinedLocation, location));
		}

		if (assetStatus != null) {
			searchCriteria.setAssetStatus(persistenceService.find(AssetStatus.class, assetStatus));
		}

		if (assetType != null) {
			searchCriteria.setAssetType(persistenceService.find(AssetType.class, assetType));
		}

		if (assetTypeGroup != null) {
			searchCriteria.setAssetTypeGroup(persistenceService.find(AssetTypeGroup.class, assetTypeGroup));
		}

		if (identifiedFrom != null || identifiedTo != null) {
			DateRange dateRange = new DateRange(RangeType.CUSTOM);

			if (identifiedFrom != null) {
				dateRange.setFromDate(identifiedFrom);
			}

			if (identifiedTo != null) {
				dateRange.setToDate(identifiedTo);
			}

			searchCriteria.setDateRange(dateRange);
		}

		// Sort column and ascending.
		String orderBy = "identified";
		boolean ascending = false;

		if(orderByField != null) {
			orderBy = orderByField.equals("name") ? "type.name" : orderByField;
		}

		if(orderByDirection != null) {
			ascending = orderByDirection.equals("ASC");
		}

		QueryBuilder<ApiModelHeader> queryBuilder = assetSearchService.augmentSearchBuilder(searchCriteria, new QueryBuilder<>(Asset.class, securityContext.getUserSecurityFilter()), false);
		queryBuilder.addOrder(orderBy, ascending);
		queryBuilder.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, "mobileGUID", "modified"));

		List<ApiModelHeader> results = persistenceService.findAll(queryBuilder);
		return results;
	}

	@GET
	@Path("details")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAsset> findAll(@QueryParam("id") List<String> assetIds) {
		QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileGUID", assetIds));
		
		List<Asset> assets = persistenceService.findAll(builder);
		List<ApiAsset> apiAssets = assets.stream().map(this::convertEntityToApiModel).collect(Collectors.toList());
		return apiAssets;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
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

	@PUT
	@Path("multiv2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void multiAddAsset(ApiAsset[] assets) {
		//In order to allow Mobile to have an easier time performing offline actions, we're accepting multi-assets as
		//a simple array.  This allows the assets to be fully created offline, then uploaded at a later time when the
		//sync process happens.
		for(ApiAsset asset : assets) {
			saveAsset(asset);
		}
	}

    @Path("{assetId}/procedures")
    public ApiProcedureDefinitionResourceV2 assetProcedureDefinitions() {
        return procedureDefinitionResource;
    }

	private Asset createAsset(ApiAsset apiAsset) {
		Asset asset = new Asset();
		converApiAsset(apiAsset, asset);
		return assetSaveService.create(asset, null, apiAsset.getImage(), "Asset.jpg");
	}
	
	private Asset updateAsset(ApiAsset apiAsset, Asset asset) {
		converApiAsset(apiAsset, asset);		
		asset.setSubAssets(apiSubAssetResource.findSubAssets(asset)); // So they don't get cleared! - WEB-3031
		return assetSaveService.update(asset, null /*null indicates attachments not updated*/, apiAsset.getImage(), "Asset.jpg", true);
	}

	private List<AssetAttachment> convertAllAttachmentsForAsset(List<ApiAssetAttachment> apiAttachments, Asset asset) {
		List<AssetAttachment> attachments = new ArrayList<AssetAttachment>();
		for (ApiAssetAttachment apiAttachment: apiAttachments) {
			if (apiAttachment.getData() != null) {
				attachments.add(apiAttachmentResource.convertApiModelToEntity(apiAttachment, asset));
			}
		}
		return attachments;
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
		apiAsset.setLastEventDate(lastEventDateService.findLastEventDate(asset));
		apiAsset.setTypeId(asset.getType().getId());
		apiAsset.setOrderNumber(asset.getNonIntergrationOrderNumber());
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
		asset.setNonIntergrationOrderNumber(apiAsset.getOrderNumber());
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
		} else {
			asset.setAssignedUser(null);
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
		
		asset.setInfoOptions(apiAttributeValueResource.convertAttributeValues(apiAsset.getAttributeValues(), asset.getInfoOptions()));
	}

    protected List<ApiAttributeValue>  findAllAttributeValues(Asset asset) {
		List<ApiAttributeValue> apiAttributeValues = new ArrayList<ApiAttributeValue>();
		
		for (InfoOptionBean option: asset.getInfoOptions()) {
			apiAttributeValues.add(apiAttributeValueResource.convertInfoOption(option));
		}
		
		return apiAttributeValues;
	}
	
	private byte[] loadAssetImage(Asset asset) {
		byte[] image = null;		
		if(asset.getImageName() != null) {
			try {
				image = s3Service.downloadAssetProfileMediumImage(asset.getId(), asset.getImageName());
			} catch (IOException ex) {
				logger.warn("Unable to load asset image for asset: " + asset.getIdentifier(), ex);
			}
		}		
		return image;
	}
}
