package com.n4systems.model.builders;

import java.util.HashSet;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

public class InfoFieldBuilder extends BaseLegacyBuilder<InfoFieldBean> {

	private final String name;
	private final Long weight;
	private final String infoFieldType;

	public static InfoFieldBuilder anInfoField() {
		return new InfoFieldBuilder("info_field_1", 1L, InfoFieldBean.TEXTFIELD_FIELD_TYPE);
	}
	
	public InfoFieldBuilder(String name, Long weight, String infoFieldType) {
		this.name = name;
		this.weight = weight;
		this.infoFieldType = infoFieldType;
	}

	public InfoFieldBuilder withName(String name) {
		return new InfoFieldBuilder(name, weight, infoFieldType);
	}
	
	@Override
	public InfoFieldBean build() {
		InfoFieldBean infoField = new InfoFieldBean();
		infoField.setUniqueID(uniqueId);
		infoField.setName(name);
		infoField.setFieldType(infoFieldType);
		infoField.setWeight(weight);
		infoField.setUnfilteredInfoOptions(new HashSet<InfoOptionBean>());
		return infoField;
	}

}
