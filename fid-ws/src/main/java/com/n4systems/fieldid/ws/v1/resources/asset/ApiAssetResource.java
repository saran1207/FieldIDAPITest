package com.n4systems.fieldid.ws.v1.resources.asset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.asset.SmartSearchWhereClause;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.util.WsServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Component
@Path("asset")
public class ApiAssetResource extends ApiResource<ApiAsset, Asset> {
	private static Logger logger = Logger.getLogger(ApiAssetResource.class);
	
	@Autowired private AssetService assetService;
	@Autowired private ApiEventHistoryResource apiEventHistoryResource;
	@Autowired private ApiEventScheduleResource apiEventScheduleResource;
	@Autowired private ApiAssetAttachmentResource apiAttachmentResource;
	
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAsset> findAll(
			@QueryParam("after") DateParam after,
			@QueryParam("searchText") String searchText,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize,
			@QueryParam("owner") Long ownerId) {
		
		QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
		builder.addOrder("created");
		
		if (ownerId != null) {
			BaseOrg owner = persistenceService.find(BaseOrg.class, ownerId);
			if (owner == null) {
				throw new NotFoundException("Organization", ownerId);
			}
			builder.applyFilter(new OwnerAndDownFilter(owner));
		}
		
		if (searchText != null) {
			builder.addWhere(new SmartSearchWhereClause(searchText, true, true, true));
		}

		List<Asset> assets = persistenceService.findAll(builder, page, pageSize);
		Long total = persistenceService.count(builder);
		
		List<ApiAsset> apiAssets = convertAllEntitiesToApiModels(assets);
		ListResponse<ApiAsset> response = new ListResponse<ApiAsset>(apiAssets, page, pageSize, total);
		return response;
	}
	
	@GET
	@Path("list")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAsset> findAll(@QueryParam("id") List<String> assetIds) {
		QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileGUID", assetIds));
		
		List<Asset> assets = persistenceService.findAll(builder);
		
		List<ApiAsset> apiAssets = convertAllEntitiesToApiModels(assets);
		ListResponse<ApiAsset> response = new ListResponse<ApiAsset>(apiAssets, 0, assets.size(), assets.size());
		return response;
	}
	
	@GET
	@Path("{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ApiAsset find(@PathParam("id") String id) {
		Asset asset = assetService.findByMobileId(id);
		if (asset == null) {
			throw new NotFoundException("Asset", id);
		}
		
		ApiAsset apiModel = convertEntityToApiModel(asset);
		return apiModel;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveAsset(ApiAsset apiAsset) {		
		Asset asset = converApiAsset(apiAsset);
		
		AssetSaveService assetSaveService = new AssetSaveService(WsServiceLocator.getLegacyAssetManager(securityContext.getTenantSecurityFilter().getTenantId()), getCurrentUser());
		assetSaveService.setUploadedAttachments(apiAttachmentResource.convertApiListToEntityList(apiAsset.getAttachments(), asset));
		assetSaveService.setAsset(asset);		
		
		if(apiAsset.getImage() != null) {
			asset.setImageName("asset.jpg");
		}
		
		asset = assetSaveService.create();
		
		if(apiAsset.getImage() != null) {
			try {
				File assetImagePath = PathHandler.getAssetImageFile(asset);
				FileUtils.writeByteArrayToFile(assetImagePath, apiAsset.getImage());
			} catch (IOException e) {
				logger.error("Error copying Asset Image", e);
			}
		}

		logger.info("Created Asset " + asset.getIdentifier());
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
		apiAsset.setSchedules(apiEventScheduleResource.findAllSchedules(asset.getId()));
		apiAsset.setEventHistory(apiEventHistoryResource.findAllEventHistory(asset.getMobileGUID()));
		apiAsset.setAttachments(apiAttachmentResource.findAllAttachments(asset.getMobileGUID()));
		
		return apiAsset;
	}
	
	private Asset converApiAsset(ApiAsset apiAsset) {
		Asset asset = new Asset();
		asset.setTenant(getCurrentTenant());
		asset.setMobileGUID(apiAsset.getSid());
		asset.setType(persistenceService.find(AssetType.class, apiAsset.getTypeId()));
		asset.setOwner(persistenceService.find(BaseOrg.class, apiAsset.getOwnerId()));
		asset.setIdentifier(apiAsset.getIdentifier());
		asset.setRfidNumber(apiAsset.getRfidNumber());
		asset.setCustomerRefNumber(apiAsset.getCustomerRefNumber());
		asset.setPurchaseOrder(apiAsset.getPurchaseOrder());
		asset.setComments(apiAsset.getComments());
		asset.setIdentified(apiAsset.getIdentified());
		asset.setNonIntergrationOrderNumber(apiAsset.getNonIntergrationOrderNumber());
		asset.setModified(apiAsset.getModified());
		
		if(apiAsset.getAssetStatusId() > 0) {
			asset.setAssetStatus(persistenceService.find(AssetStatus.class, apiAsset.getAssetStatusId()));
		}
		
		if(apiAsset.getIdentifiedById() > 0) {
			asset.setIdentifiedBy(persistenceService.find(User.class, apiAsset.getIdentifiedById()));
		}
		
		if(apiAsset.getAssignedUserId() > 0) {
			asset.setAssignedUser(persistenceService.find(User.class, apiAsset.getAssignedUserId()));
		}
		
		if(apiAsset.getGpsLatitude() != null && apiAsset.getGpsLongitude() != null) {
			GpsLocation gpsLocation = new GpsLocation(apiAsset.getGpsLatitude(), apiAsset.getGpsLongitude());
			
			if(gpsLocation.isValid()) {
				asset.setGpsLocation(gpsLocation);
			}
		}
		
		if(apiAsset.getFreeformLocation() != null || apiAsset.getPredefinedLocationId() != null) {
			Location location = new Location();
			location.setFreeformLocation(apiAsset.getFreeformLocation());
			
			if(apiAsset.getPredefinedLocationId() != null) {
				location.setPredefinedLocation(persistenceService.find(PredefinedLocation.class, apiAsset.getPredefinedLocationId()));
			}
			
			asset.setAdvancedLocation(location);
		}
		
		asset.setInfoOptions(convertAttributeValues(apiAsset.getAttributeValues()));
		
		return asset;
	}
	
	private List<ApiAttributeValue>  findAllAttributeValues(Asset asset) {
		List<ApiAttributeValue> apiAttributeValues = new ArrayList<ApiAttributeValue>();
		
		for (InfoOptionBean option: asset.getInfoOptions()) {
			apiAttributeValues.add(convertInfoOption(option));
		}
		
		return apiAttributeValues;
	}
	
	private Set<InfoOptionBean> convertAttributeValues(List<ApiAttributeValue> apiAttributeValues) {
		Set<InfoOptionBean> infoOptions = new TreeSet<InfoOptionBean>();
		
		for(ApiAttributeValue apiAttributeValue : apiAttributeValues) {
			infoOptions.add(convertApiAttributeValue(apiAttributeValue));
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
	
	private InfoOptionBean convertApiAttributeValue(ApiAttributeValue apiAttributeValue) {
		InfoFieldBean infoField = persistenceService.findNonSecure(InfoFieldBean.class, apiAttributeValue.getAttributeId());
		
		Object value = apiAttributeValue.getValue();
		
		InfoOptionBean infoOptionBean = null;
		if (value instanceof Date) {
			infoOptionBean = new InfoOptionBean();
			infoOptionBean.setInfoField(infoField);
			infoOptionBean.setName(Long.toString(((Date) value).getTime()));
		} else {
			Set<InfoOptionBean> staticOptions = infoField.getUnfilteredInfoOptions();
			for (InfoOptionBean staticOption: staticOptions) {
				if (staticOption.getName().equals(value)) {
					infoOptionBean = staticOption;
					break;
				}
			}
			
			if (infoOptionBean == null) {
				infoOptionBean = new InfoOptionBean();
				infoOptionBean.setInfoField(infoField);
				infoOptionBean.setName(value.toString());
			}
		}
		return infoOptionBean;
	}
	
	private byte[] loadAssetImage(Asset asset) {
		byte[] image = null;
		
		File assetImageFile = PathHandler.getAssetImageFile(asset);
		if (assetImageFile.exists()) {
			try {
				image = FileUtils.readFileToByteArray(assetImageFile);
			} catch(Exception e) {
				logger.warn("Unable to load asset image at: " + assetImageFile, e);
			}
		}
		return image;
	}
	
}
