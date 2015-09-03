package com.n4systems.fieldid.ws.v2.resources.customerdata.asset;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.customerdata.asset.attributevalues.ApiAttributeValueConverter;
import com.n4systems.model.*;
import com.n4systems.model.asset.SmartSearchWhereClause;
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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Path("asset")
public class ApiAssetResource extends ApiResource<ApiAsset, Asset> {
	
	@Autowired private AssetService assetService;
	@Autowired private ApiAttributeValueConverter apiAttributeValueResource;
	@Autowired private AssetSearchService assetSearchService;

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> query(@QueryParam("id") List<ApiKeyString> assetIds) {
		if (assetIds.isEmpty()) return new ArrayList<>();

		QueryBuilder<ApiModelHeader> query = new QueryBuilder<>(Asset.class, securityContext.getUserSecurityFilter());
		query.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, "mobileGUID", "modified"));
		query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", unwrapKeys(assetIds)));
		List<ApiModelHeader> results = persistenceService.findAll(query);
		return results;
	}

	@GET
	@Path("query/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAssetModelHeader> querySearch(
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
			@QueryParam("orderByField") @DefaultValue("identified") String orderByField,
			@QueryParam("orderByDirection") @DefaultValue("DESC") String orderByDirection) {

		List<ApiAssetModelHeader> results = persistenceService.findAll(createAssetSearchQuery(
				rfidNumber, identifier, referenceNumber, assignedTo,
				owner, location, predefinedLocationId, orderNumber, purchaseOrder,
				assetStatus, assetType, assetTypeGroup, identifiedFrom, identifiedTo,
				orderByField, orderByDirection));
		return results;
	}

	@GET
	@Path("query/smartSearch")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAssetModelHeader> querySmartSearch(@QueryParam("searchText") String searchText) {
		List<ApiAssetModelHeader> results = persistenceService.findAll(createAssetSmartSearchQuery(searchText));
		return results;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAsset> findAll(@QueryParam("id") List<ApiKeyString> assetIds) {
		if (assetIds.isEmpty()) return new ArrayList<>();

		List<ApiAsset> apiAssets = convertAllEntitiesToApiModels(assetService.findByMobileId(unwrapKeys(assetIds)));
		return apiAssets;
	}

	private QueryBuilder<ApiAssetModelHeader> createAssetSearchQuery(String rfidNumber, String identifier, String referenceNumber, Long assignedTo, Long owner, String location, Long predefinedLocationId, String orderNumber, String purchaseOrder, Long assetStatus, Long assetType, Long assetTypeGroup, Date identifiedFrom, Date identifiedTo, String orderByField, String orderByDirection) {
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

		if (orderByField.equals("name")) {
			orderByField = "type.name";
		}

		QueryBuilder<ApiAssetModelHeader> queryBuilder = assetSearchService.augmentSearchBuilder(searchCriteria, new QueryBuilder<>(Asset.class, securityContext.getUserSecurityFilter()), false);
		queryBuilder.setSelectArgument(new NewObjectSelect(ApiAssetModelHeader.class, "mobileGUID", "modified", orderByField, "identifier"));
		queryBuilder.addOrder(orderByField, orderByDirection.equals("ASC"));
		return queryBuilder;
	}


	private QueryBuilder<ApiAssetModelHeader> createAssetSmartSearchQuery(String searchText) {
		QueryBuilder<ApiAssetModelHeader> queryBuilder = new QueryBuilder<>(ApiAssetModelHeader.class, securityContext.getUserSecurityFilter());
		queryBuilder.setSelectArgument(new NewObjectSelect(ApiAssetModelHeader.class, "mobileGUID", "modified", "created", "identifier"));
		queryBuilder.addOrder("created");
		queryBuilder.addWhere(new SmartSearchWhereClause(searchText, true, true, true));

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
		apiAsset.setMasterAsset(findMasterAsset(asset));
		apiAsset.setSubAssets(findAndConvertSubAssets(asset));
		
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
