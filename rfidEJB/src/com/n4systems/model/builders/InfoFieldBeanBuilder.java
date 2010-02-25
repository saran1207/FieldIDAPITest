package com.n4systems.model.builders;

import rfid.ejb.entity.InfoFieldBean;

public class InfoFieldBeanBuilder extends BaseBuilder<InfoFieldBean> {

	private final String name;
	private final String type;
	
	public static InfoFieldBeanBuilder aTextField() {
		return new InfoFieldBeanBuilder("some field", InfoFieldBean.TEXTFIELD_FIELD_TYPE);
	}
	
	private InfoFieldBeanBuilder(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	
	public InfoFieldBeanBuilder withName(String string) {
		return new InfoFieldBeanBuilder(name, type);
	}

	@Override
	public InfoFieldBean build() {
		InfoFieldBean field =  new InfoFieldBean();
		field.setName(name);
		field.setFieldType(type);
		field.setUniqueID(id);
		return field;
	}


	


	

}
