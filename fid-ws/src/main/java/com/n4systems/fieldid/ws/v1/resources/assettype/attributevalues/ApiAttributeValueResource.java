package com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class ApiAttributeValueResource {

    @Autowired
    private AssetTypeService assetTypeService;

    //Changed to support the new fields in ApiAttributeValue
	public ApiAttributeValue convertInfoOption(InfoOptionBean infoOption) {
		ApiAttributeValue attribValue = new ApiAttributeValue();
		attribValue.setAttributeId(infoOption.getUniqueID());
        attribValue.setAssetTypeId(infoOption.getInfoField().getAssetInfo().getId());
        attribValue.setName(infoOption.getInfoField().getName());
        attribValue.setFieldType(infoOption.getInfoField().getFieldType());

        attribValue.setValue(infoOption.getName());
		
//		if (infoOption.getInfoField().isDateField()) {
//			if (infoOption.getName() != null) {
//				attribValue.setValue(new Date(Long.parseLong(infoOption.getName())));
//			}
//		} else {
//			attribValue.setValue(infoOption.getName());
//		}

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

    public List<InfoOptionBean> convertAttributes(List<ApiAttributeValue> attributes) {
        List<InfoOptionBean> infoOptionBeans = Lists.newArrayList();
        for(ApiAttributeValue attribute : attributes) {
            infoOptionBeans.add(convertAttribute(attribute));
        }
        return infoOptionBeans;
    }

    private InfoOptionBean convertAttribute(ApiAttributeValue attribute) {
        InfoOptionBean infoOptionBean = new InfoOptionBean();
        InfoFieldBean infoFieldBean = new InfoFieldBean();

        infoOptionBean.setUniqueID(attribute.getAttributeId()); //This is where it should be set.

        infoFieldBean.setFieldType(attribute.getFieldType());
        infoOptionBean.setName(attribute.getValue());
        infoFieldBean.setName(attribute.getName());

        //If this fails, we need to kick out a failure that stops processing and kicks back an error.
        infoFieldBean.setAssetInfo(assetTypeService.getAssetType(attribute.getAssetTypeId()));

        return infoOptionBean;
    }
}
