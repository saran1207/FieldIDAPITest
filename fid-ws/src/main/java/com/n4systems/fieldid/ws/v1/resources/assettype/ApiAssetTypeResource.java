package com.n4systems.fieldid.ws.v1.resources.assettype;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributes.*;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("assetType")
public class ApiAssetTypeResource extends SetupDataResource<ApiAssetType, AssetType> {
	private static Logger logger = Logger.getLogger(ApiAssetTypeResource.class);

    @Autowired private S3Service s3Service;

	public ApiAssetTypeResource() {
		super(AssetType.class, true);
	}

	@Override
	protected ApiAssetType convertEntityToApiModel(AssetType type) {
		ApiAssetType apiType = new ApiAssetType();
		apiType.setSid(type.getId());
		apiType.setActive(type.isActive());
		apiType.setModified(type.getModified());


		
		// We only set the rest of the fields if the entity is active.
		if(type.isActive()) {
			apiType.setName(type.isActive() ? type.getName() : type.getArchivedName());
			apiType.setWarnings(type.getWarnings());
			apiType.setInstructions(type.getInstructions());
			apiType.setCautionUrl(type.getCautionUrl());
			apiType.setDescriptionTemplate(type.getDescriptionTemplate());
            apiType.setHasProcedures(type.hasProcedures());
			apiType.setIdentifierFormat(type.getIdentifierFormat());
			apiType.setIdentifierLabel(type.getIdentifierLabel());
			apiType.setIdentifierOverridden(type.isIdentifierOverridden());
			apiType.setImage(loadAssetTypeImage(type));
			apiType.setGroup(convertAssetTypeGroup(type));
			apiType.setLinkable(type.isLinkable());
			
			for (AssociatedEventType associatedEventType: type.getAssociatedEventTypes()) {
				apiType.getAllowedEventTypes().add(associatedEventType.getEventType().getId());
			}
			
			for (AssetTypeSchedule schedule: type.getSchedules()) {
				apiType.getSchedules().add(convertAssetTypeSchedule(schedule));
			}
			
			for (InfoFieldBean field: type.getInfoFields()) {
				apiType.getAttributes().add(convertInfoFieldBean(field));
			}
		}
		
		return apiType;
	}
	
	private ApiAttribute convertInfoFieldBean(InfoFieldBean field) {
		ApiAttribute attrib;
		switch (field.getType()) {
			case SelectBox:
				attrib = new ApiSelectBoxAttribute(convertInfoOptions(field.getInfoOptions()));
				break;
			case ComboBox:
				attrib = new ApiComboBoxAttribute(convertInfoOptions(field.getInfoOptions()));
				break;
			case DateField:
				attrib = new ApiDateTimeAttribute(field.isIncludeTime());
				break;
			case UnitOfMeasure:
				attrib = new ApiUnitAttribute(field.getUnitOfMeasure().getId());
				break;
			case TextField:
				attrib = new ApiTextBoxAttribute();
				break;
			default:
				throw new IllegalArgumentException("Unhandled InfoFieldType: " + field.getType().name());
		}
		attrib.setSid(field.getUniqueID());
		attrib.setActive(!field.isRetired());
		attrib.setName(field.getName());
		attrib.setWeight(field.getWeight());
		attrib.setRequired(field.isRequired());
		return attrib;
	}
	
	private List<ApiAttributeOption> convertInfoOptions(List<InfoOptionBean> options) {
		List<ApiAttributeOption> apiOptions = new ArrayList<ApiAttributeOption>();
		for (InfoOptionBean option: options) {
			apiOptions.add(convertInfoOption(option));
		}
		return apiOptions;
	}
	
	private ApiAttributeOption convertInfoOption(InfoOptionBean option) {
		ApiAttributeOption apiOption = new ApiAttributeOption();
		apiOption.setSid(option.getUniqueID());
		apiOption.setActive(true);
		apiOption.setValue(option.getName());
		apiOption.setWeight(option.getWeight());
		return apiOption;
	}

	private ApiAssetTypeSchedule convertAssetTypeSchedule(AssetTypeSchedule schedule) {
		ApiAssetTypeSchedule apiSchedule = new ApiAssetTypeSchedule();
		apiSchedule.setOwnerId(schedule.getOwner().getId());
		apiSchedule.setEventTypeId(schedule.getEventType().getId());
		apiSchedule.setFrequency(schedule.getFrequency());
		apiSchedule.setAutoSchedule(schedule.isAutoSchedule());
		return apiSchedule;
	}

	private ApiAssetTypeGroup convertAssetTypeGroup(AssetType type) {
		ApiAssetTypeGroup apiGroup = null;
		if (type.getGroup() != null) {
			apiGroup = new ApiAssetTypeGroup();
			apiGroup.setSid(type.getGroup().getId());
			apiGroup.setActive(true);
			apiGroup.setModified(type.getGroup().getModified());
			apiGroup.setName(type.getGroup().getName());
			apiGroup.setWeight(type.getGroup().getOrderIdx());
            apiGroup.setLotoDevice(type.getGroup().isLotoDevice());
            apiGroup.setLotoLock(type.getGroup().isLotoLock());
		}
		return apiGroup;
	}

	private byte[] loadAssetTypeImage(AssetType type) {
		byte[] image = null;
		if (type.hasImage()) {
            if(s3Service.assetTypeProfileImageExists(type)){
                try {
                    image = s3Service.downloadAssetTypeProfileImageBytes(type);
                } catch (IOException ex) {
                    logger.warn("Unable to download asset type from S3 image for asset: " + type.getId(), ex);
                }
            }
		}
		return image;
	}

}
