package com.n4systems.model.builders;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

public class InfoOptionBeanBuilder extends BaseBuilder<InfoOptionBean> {

	private final boolean dynamicOption;
	private final String name;
	private final InfoFieldBean field;

	public static InfoOptionBeanBuilder aStaticInfoOption() {
		return anInfoOption(false); 
	}
	
	public static InfoOptionBeanBuilder aDynamicInfoOption() {
		return anInfoOption(true);
	}
	
	private static InfoOptionBeanBuilder anInfoOption(boolean dynamicOption) {
		return new InfoOptionBeanBuilder(dynamicOption, "some option", InfoFieldBeanBuilder.aTextField().build());
	}
	
	private InfoOptionBeanBuilder(boolean dynamicOption, String name, InfoFieldBean field) {
		this.dynamicOption = dynamicOption;
		this.name = name;
		this.field = field;
	}
	
	public InfoOptionBeanBuilder withName(String name) {
		return new InfoOptionBeanBuilder(dynamicOption, name, field);
	}
	
	public InfoOptionBeanBuilder forField(InfoFieldBean field) {
		return new InfoOptionBeanBuilder(dynamicOption, name, field);
	}
	
	@Override
	public InfoOptionBean createObject() {
		InfoOptionBean option = new InfoOptionBean();
		
		option.setName(name);
		option.setInfoField(field);
		option.setStaticData(!dynamicOption);
		option.setUniqueID(getId());
		
		return option;
	}
	
}
