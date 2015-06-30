package com.n4systems.fieldid.ws.v2.resources.asset;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.asset.attributevalues.ApiAttributeValueResource;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Component
@Path("asset")
public class ApiAssetResource extends ApiResource<ApiAsset, Asset> {
	
	@Autowired private AssetService assetService;
	@Autowired private ApiSubAssetResource apiSubAssetResource;
	@Autowired private ApiAttributeValueResource apiAttributeValueResource;
	@Autowired private AssetSearchService assetSearchService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryAssets(
			@QueryParam("id") List<String> assetIds,
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


		QueryBuilder<ApiModelHeader> builder;
		if (assetIds != null) {
			builder = new QueryBuilder<>(Asset.class, securityContext.getUserSecurityFilter());
			builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", assetIds));
		} else {
			builder = createAssetSearchQuery(rfidNumber, identifier, referenceNumber, assignedTo, owner, location, predefinedLocationId, orderNumber, purchaseOrder, assetStatus, assetType, assetTypeGroup, identifiedFrom, identifiedTo, orderByField, orderByDirection);
		}

		List<ApiModelHeader> results = persistenceService.findAll(builder);
		return results;
	}

	@GET
	@Path("details")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAsset> findAll(@QueryParam("id") List<String> assetIds) {
		List<ApiAsset> apiAssets = convertAllEntitiesToApiModels(assetService.findByMobileId(assetIds));
		return apiAssets;
	}

	private QueryBuilder<ApiModelHeader> createAssetSearchQuery(@QueryParam("rfidNumber") String rfidNumber, @QueryParam("identifier") String identifier, @QueryParam("referenceNumber") String referenceNumber, @QueryParam("assignedTo") Long assignedTo, @QueryParam("owner") Long owner, @QueryParam("location") String location, @QueryParam("predefinedLocationId") Long predefinedLocationId, @QueryParam("orderNumber") String orderNumber, @QueryParam("purchaseOrder") String purchaseOrder, @QueryParam("assetStatus") Long assetStatus, @QueryParam("assetType") Long assetType, @QueryParam("assetTypeGroup") Long assetTypeGroup, @QueryParam("identifiedFrom") Date identifiedFrom, @QueryParam("identifiedTo") Date identifiedTo, @QueryParam("orderByField") String orderByField, @QueryParam("orderByDirection") String orderByDirection) {
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
		return queryBuilder;
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
		apiAsset.setOrderNumber(asset.getNonIntergrationOrderNumber());
		apiAsset.setImage(assetService.loadAssetProfileImage(asset));
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
		apiAsset.setAttributeValues(apiAttributeValueResource.convertInfoOptions(asset.getInfoOptions()));
		
		return apiAsset;
	}

}
