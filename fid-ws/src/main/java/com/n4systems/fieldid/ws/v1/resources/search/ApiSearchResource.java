package com.n4systems.fieldid.ws.v1.resources.search;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.*;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@Path("search")
public class ApiSearchResource extends ApiResource<ApiSearchResult, Asset> {
	private static Logger logger = Logger.getLogger(ApiSearchResource.class);

	@Autowired private AssetSearchService assetSearchService;
	@Autowired private EventScheduleService eventScheduleService;
	@Autowired private S3Service s3service;
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiSearchResult> getSmartSearchSuggestions(
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("25") @QueryParam("pageSize") int pageSize,
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
		
		QueryBuilder<Asset> query = assetSearchService.createBaseSearchQueryBuilder(searchCriteria).addOrder(orderBy, ascending);
		List<Asset> results = persistenceService.findAll(query, page, pageSize);
		Long total = persistenceService.count(query);
		
		List<ApiSearchResult> apiResults = convertAllEntitiesToApiModels(results);
		ListResponse<ApiSearchResult> response = new ListResponse<ApiSearchResult>(apiResults, page, pageSize, total);
		return response;
	}

	@Override
	protected ApiSearchResult convertEntityToApiModel(Asset asset) {
		ApiSearchResult apiResult = new ApiSearchResult();
		apiResult.setSid(asset.getMobileGUID());
		apiResult.setAssetTypeName(asset.getType().getName());
		apiResult.setIdentifier(asset.getIdentifier());
		apiResult.setReferenceNumber(asset.getCustomerRefNumber());
		apiResult.setAssetStatus(asset.getAssetStatus() != null ? asset.getAssetStatus().getName() : null);
		apiResult.setDescription(asset.getDescription());
		apiResult.setInternalOwnerName(asset.getOwner().getInternalOrg().getName());
		apiResult.setCustomerOwnerName(asset.getOwner().getCustomerOrg() != null ? asset.getOwner().getCustomerOrg().getName() : null);
		apiResult.setDivisionOwnerName(asset.getOwner().getDivisionOrg() != null ? asset.getOwner().getDivisionOrg().getName() : null);
		apiResult.setImage(loadAssetImage(asset));

		if(asset.getAdvancedLocation() != null) {
			apiResult.setLocation(asset.getAdvancedLocation().getFreeformLocation());
			if (asset.getAdvancedLocation().getPredefinedLocation() != null) {
				apiResult.setPredefinedLocationId(asset.getAdvancedLocation().getPredefinedLocation().getId());
			}
		}
		
		Event openEvent = eventScheduleService.getNextEventSchedule(asset.getId(), null);
		if (openEvent != null) {
			apiResult.setNextEventDate(openEvent.getDueDate());
		}
		
		return apiResult;
	}

	private byte[] loadAssetImage(Asset asset) {
		byte[] image = null;
		if(asset.getImageName() != null) {
			try {
				image = s3service.downloadAssetProfileMediumImage(asset.getId(), asset.getImageName());
			} catch (IOException ex) {
				logger.warn("Unable to load asset image for asset: " + asset.getIdentifier(), ex);
			}
		}
		return image;
	}
}
