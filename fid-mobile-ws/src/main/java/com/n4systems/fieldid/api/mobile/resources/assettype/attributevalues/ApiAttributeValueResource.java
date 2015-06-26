package com.n4systems.fieldid.api.mobile.resources.assettype.attributevalues;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.*;

@Transactional
public class ApiAttributeValueResource extends FieldIdPersistenceService {

    //Changed to support the new fields in ApiAttributeValue
	public ApiAttributeValue convertInfoOption(InfoOptionBean infoOption) {
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
	
	// I had to explicitly write this method instead of extending ApiResource<A, E extends AbstractEntity> 
	// Because InfoOptionBean doesn't extend AbstractEntity. Not sure if we need to adjust the model at this point.
	public List<ApiAttributeValue> convertInfoOptions(List<InfoOptionBean> infoOptions) {
		List<ApiAttributeValue> attribValues = new ArrayList<ApiAttributeValue>();
		for (InfoOptionBean infoOption: infoOptions) {
			attribValues.add(convertInfoOption(infoOption));
		}
		return attribValues;
	}

	public Set<InfoOptionBean> convertAttributeValues(List<ApiAttributeValue> apiAttributeValues, Collection<InfoOptionBean> existingOptions) {
		Set<InfoOptionBean> infoOptions = new TreeSet<InfoOptionBean>();
		for(ApiAttributeValue apiAttributeValue : apiAttributeValues) {
			infoOptions.add(convertApiAttributeValue(apiAttributeValue, existingOptions));
		}
		return infoOptions;
	}

	// Convert a given attributevalue into InfoOptionBean of date, static(select, combobox) or text type.
	// During Edit, because we are not storing InfoOptionId in client side, we have to look up by infoFieldID for a given asset.
	public InfoOptionBean convertApiAttributeValue(ApiAttributeValue apiAttributeValue, Collection<InfoOptionBean> existingOptions) {
		Long infoFieldID = apiAttributeValue.getAttributeId();
		Object value = apiAttributeValue.getValue();
		InfoFieldBean infoField;

		// Get the infoOption from the existing assetInfoOptions if we have a match.
		InfoOptionBean infoOptionBean = null;

		if (existingOptions != null) {
			for (InfoOptionBean infoOption: existingOptions) {
				if(infoOption.getInfoField().getUniqueID().equals(infoFieldID)) {
					infoOptionBean = infoOption;
					break;
				}
			}
		}

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

			if (staticOption != null) {
				//TODO, We need to remove the existing infoOptionBean if there was one at this point! Ask Mark.
				infoOptionBean = staticOption;
			} else {
				if (infoOptionBean.isStaticData()) {
					infoOptionBean = new InfoOptionBean();
					infoOptionBean.setInfoField(infoField);
				}
				infoOptionBean.setName(value.toString());
			}
		}

		return infoOptionBean;
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

}
