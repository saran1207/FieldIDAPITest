package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

public class InfoFieldBeanBuilder extends BaseBuilder<InfoFieldBean> {

	private final String name;
	private final String type;
	private final Set<InfoOptionBean> options;
	private final boolean required;
	
	public static InfoFieldBeanBuilder aTextField() {
		return new InfoFieldBeanBuilder("some field", InfoFieldBean.TEXTFIELD_FIELD_TYPE, false, new TreeSet<InfoOptionBean>());
	}
	
	public static InfoFieldBeanBuilder aComboBox() {
		return new InfoFieldBeanBuilder("some field", InfoFieldBean.COMBOBOX_FIELD_TYPE, false, new TreeSet<InfoOptionBean>());
	}
	
	public static InfoFieldBeanBuilder aSelectBox() {
		return new InfoFieldBeanBuilder("some field", InfoFieldBean.SELECTBOX_FIELD_TYPE, false, new TreeSet<InfoOptionBean>());
	}
	
	private InfoFieldBeanBuilder(String name, String type, boolean required, Set<InfoOptionBean> options) {
		super();
		this.name = name;
		this.type = type;
		this.required = required;
		this.options = options;
	}

	
	public InfoFieldBeanBuilder named(String name) {
		return new InfoFieldBeanBuilder(name, type, required, options);
	}
	
	public InfoFieldBeanBuilder withOptions(InfoOptionBean ... options) {
		return new InfoFieldBeanBuilder(name, type, required, new TreeSet<InfoOptionBean>(Arrays.asList(options)));
	}

	public InfoFieldBeanBuilder setRequired(boolean required) {
		return new InfoFieldBeanBuilder(name, type, required, options);
	}
	
	@Override
	public InfoFieldBean build() {
		InfoFieldBean field =  new InfoFieldBean();
		field.setName(name);
		field.setFieldType(type);
		field.setUniqueID(id);
		field.setRequired(required);
		field.setUnfilteredInfoOptions(options);
		field.associateOptions();
		
		return field;
	}

	

	


	

}
