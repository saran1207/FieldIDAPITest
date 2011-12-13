package com.n4systems.fieldid.ws.v1.resources.asset;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues.ApiComboBoxAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues.ApiDateTimeAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues.ApiSelectBoxAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues.ApiTextBoxAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.eventtype.attributevalues.ApiUnitAttributeValue;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;

@Component
@Path("asset")
public class ApiAssetResource extends ApiResource<ApiAsset, Asset> {
	private static Logger logger = Logger.getLogger(ApiAssetResource.class);
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAsset> findAll(
			@QueryParam("after") DateParam after,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("100") @QueryParam("pageSize") int pageSize,
			@QueryParam("owner") Long ownerId) {
		
		QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
		
		if (ownerId != null) {
			BaseOrg owner = persistenceService.find(BaseOrg.class, ownerId);
			if (owner == null) {
				throw new NotFoundException("Organization", ownerId);
			}
			builder.applyFilter(new OwnerAndDownFilter(owner));
		}

		List<Asset> assets = persistenceService.findAll(builder, page, pageSize);
		Long total = persistenceService.count(builder);
		
		List<ApiAsset> apiAssets = convertAllEntitiesToApiModels(assets);
		ListResponse<ApiAsset> response = new ListResponse<ApiAsset>(apiAssets, page, pageSize, total);
		return response;
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
		
		return apiAsset;
	}
	
	private ApiAttributeValue convertInfoOption(InfoOptionBean infoOption) {
		ApiAttributeValue attribValue;
		switch (infoOption.getInfoField().getType()) {
			case SelectBox:
				attribValue = new ApiSelectBoxAttributeValue();
				((ApiSelectBoxAttributeValue) attribValue).setOptionId(infoOption.getUniqueID());
				break;
			case ComboBox:
				attribValue = new ApiComboBoxAttributeValue();
				if (infoOption.isStaticData()) {
					((ApiComboBoxAttributeValue) attribValue).setOptionId(infoOption.getUniqueID());
				} else {
					((ApiComboBoxAttributeValue) attribValue).setText(infoOption.getName());
				}
				break;
			case DateField:
				attribValue = new ApiDateTimeAttributeValue();
				if (infoOption.getName() != null) {
					((ApiDateTimeAttributeValue) attribValue).setDate(new Date(Long.parseLong(infoOption.getName())));
				}
				break;
			case UnitOfMeasure:
				attribValue = new ApiUnitAttributeValue();
				((ApiUnitAttributeValue) attribValue).setText(infoOption.getName());
				break;
			case TextField:
				attribValue = new ApiTextBoxAttributeValue();
				((ApiTextBoxAttributeValue) attribValue).setText(infoOption.getName());
				break;
			default:
				throw new IllegalArgumentException("Unhandled InfoFieldType: " + infoOption.getInfoField().getType().name());
		}
		attribValue.setAttributeId(infoOption.getInfoField().getUniqueID());
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
