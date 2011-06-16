package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.util.StringListingPair;


public class InfoFieldInput {

	private Long uniqueID;
	private String name;
	private Long weight;
	private String fieldType;
	private boolean required;
	private boolean deleted;
	private boolean retired;
	private boolean includeTime;
	
	private Long defaultUnitOfMeasure; 
	
	private InfoFieldBean infoField;

	public InfoFieldInput() {
		super();
	}
	
	public InfoFieldInput( InfoFieldBean infoField ) {
		super();
		uniqueID = infoField.getUniqueID();
		name = infoField.getName();
		weight = infoField.getWeight();
		
		if( infoField.getFieldType().equals( InfoFieldBean.COMBOBOX_FIELD_TYPE  ) ){
			fieldType = InfoFieldBean.InfoFieldType.ComboBox.name(); 
		} else if( infoField.getFieldType().equals( InfoFieldBean.SELECTBOX_FIELD_TYPE  ) ){
			fieldType = InfoFieldBean.InfoFieldType.SelectBox.name(); 
		} else if( infoField.getFieldType().equals( InfoFieldBean.TEXTFIELD_FIELD_TYPE ) ){
			if( infoField.isUsingUnitOfMeasure() ) {
				fieldType = InfoFieldBean.InfoFieldType.UnitOfMeasure.name();
			} else {
				fieldType = InfoFieldBean.InfoFieldType.TextField.name();
			}
		} else if( infoField.getFieldType().equals( InfoFieldBean.DATEFIELD_FIELD_TYPE  ) ){
			fieldType = InfoFieldBean.InfoFieldType.DateField.name(); 
		} 
		required = infoField.isRequired();
		deleted = false;
		retired = infoField.isRetired();
		includeTime = infoField.isIncludeTime();
		this.infoField = infoField;
		defaultUnitOfMeasure = ( infoField.getUnitOfMeasure() != null ) ? infoField.getUnitOfMeasure().getId() : null;
		
	}
	
	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}



	

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public InfoFieldBean getInfoField() {
		return infoField;
	}

	public void setInfoField(InfoFieldBean infoField) {
		this.infoField = infoField;
	}

	public boolean isRetired() {
		return retired;
	}

		
	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	public void setInfoFieldFieldType( InfoFieldBean field ) {
		InfoFieldBean.InfoFieldType type = InfoFieldBean.InfoFieldType.valueOf( fieldType );
		switch ( type ) {
			case ComboBox:
				field.setFieldType( InfoFieldBean.COMBOBOX_FIELD_TYPE );
				break;
			case SelectBox:
				field.setFieldType( InfoFieldBean.SELECTBOX_FIELD_TYPE );
				break;
			case UnitOfMeasure:
				field.setFieldType( InfoFieldBean.TEXTFIELD_FIELD_TYPE );
				field.setUsingUnitOfMeasure( true );
				break;
			case TextField:
				field.setFieldType( InfoFieldBean.TEXTFIELD_FIELD_TYPE );
				field.setUsingUnitOfMeasure( false );
				break;
			case DateField:
				field.setFieldType( InfoFieldBean.DATEFIELD_FIELD_TYPE );
				break;
		}
	}
	
	public static List<StringListingPair> getComboBoxInfoOptions( InfoFieldBean field, InfoOptionInput inputOption ) {
		List<StringListingPair> comboBoxInfoOptions = new ArrayList<StringListingPair>();
		if( inputOption != null ) {
			field.createComboBoxInfoOptions( inputOption.convertToInfoOptionBean( field ) );
		}
		
		for( InfoOptionBean option : field.getComboBoxInfoOptions() ) {
			comboBoxInfoOptions.add( InfoOptionInput.convertInfoOptionToLP( option ) );
		}
		
		return comboBoxInfoOptions;
	}

	public Long getDefaultUnitOfMeasure() {
		return defaultUnitOfMeasure;
	}

	public void setDefaultUnitOfMeasure( Long defaultUnitOfMeasure ) {
		this.defaultUnitOfMeasure = defaultUnitOfMeasure;
	}

	public boolean hasStaticOptions() {
		
		InfoFieldBean infoFieldBean = new InfoFieldBean();
		setInfoFieldFieldType(infoFieldBean);
		
		return infoFieldBean.hasStaticInfoOption();
	}

	public boolean isIncludeTime() {
		return includeTime;
	}

	public void setIncludeTime(boolean includeTime) {
		this.includeTime = includeTime;
	}
	
}
