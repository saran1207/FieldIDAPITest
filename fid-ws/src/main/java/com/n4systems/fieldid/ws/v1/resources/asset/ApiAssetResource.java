package com.n4systems.fieldid.ws.v1.resources.asset;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.assetattachment.ApiAssetAttachment;
import com.n4systems.fieldid.ws.v1.resources.assetattachment.ApiAssetAttachmentResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiEventHistory;
import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiEventHistoryResource;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventSchedule;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.asset.SmartSearchWhereClause;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Component
@Path("asset")
public class ApiAssetResource extends ApiResource<ApiAsset, Asset> {
	private static Logger logger = Logger.getLogger(ApiAssetResource.class);
	
	@Autowired private AssetService assetService;
	@Autowired private EventScheduleService eventScheduleService;
	@Autowired private ApiAssetAttachmentResource apiAttachmentResource;
	@Autowired private ApiEventHistoryResource apiEventHistoryResource;
	
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
				apiAsset.setGpsLatitude(asset.getGpsLocation().getLatitude().longValue());
			}
			
			if (asset.getGpsLocation().getLongitude() != null) {
				apiAsset.setGpsLongitude(asset.getGpsLocation().getLongitude().longValue());
			}
		}
		
		if (asset.getAdvancedLocation() != null) {
			apiAsset.setFreeformLocation(asset.getAdvancedLocation().getFreeformLocation());
			
			if (asset.getAdvancedLocation().getPredefinedLocation() != null) {
				apiAsset.setPredefinedLocationId(asset.getAdvancedLocation().getPredefinedLocation().getId());
			}
		}
		
		for (InfoOptionBean option: asset.getInfoOptions()) {
			apiAsset.getAttributeValues().add(convertInfoOption(option));
		}
		
		List<EventSchedule> schedules = eventScheduleService.getIncompleteSchedules(asset.getId());
		for (EventSchedule schedule: schedules) {
			apiAsset.getSchedules().add(convertEventSchedule(schedule));
		}
		
		List<ApiAssetAttachment> apiAttachments = apiAttachmentResource.findAllAttachments(asset.getMobileGUID());		
		for (ApiAssetAttachment apiAttachment : apiAttachments) {
			// If attachment is not an image, remove the data. User has to get that data on fly.
			if(!apiAttachment.isImage()) {
				apiAttachment.setData(null);
			}
		}		
		apiAsset.setAttachments(apiAttachments);
		
		List<ApiEventHistory> eventHistory = apiEventHistoryResource.findAllEventHistory(asset.getMobileGUID());
		apiAsset.setEventHistory(eventHistory);
		
		return apiAsset;
	}
	
	private ApiEventSchedule convertEventSchedule(EventSchedule schedule) {
		ApiEventSchedule apiSchedule = new ApiEventSchedule();
		apiSchedule.setSid(schedule.getMobileGUID());
		apiSchedule.setActive(true);
		apiSchedule.setModified(schedule.getModified());
		apiSchedule.setOwnerId(schedule.getOwner().getId());
		apiSchedule.setAssetId(schedule.getAsset().getMobileGUID());
		apiSchedule.setEventTypeId(schedule.getEventType().getId());
		apiSchedule.setEventTypeName(schedule.getEventType().getName());
		apiSchedule.setNextDate(schedule.getNextDate());
		return apiSchedule;
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
