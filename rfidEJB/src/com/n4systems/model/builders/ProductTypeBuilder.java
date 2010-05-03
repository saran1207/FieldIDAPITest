package com.n4systems.model.builders;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.exceptions.Defect;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;

public class ProductTypeBuilder extends BaseBuilder<ProductType> {

	private final String name;
	private final String warnings;
    private final String instructions;
    private final String cautionsURL;
    private final boolean manufactureCertificate;
    private final String descriptionTemplate;
    private final ProductTypeGroup group;
    private final Collection<InfoFieldBean> infoFields;
    private final Set<ProductType> subTypes;
	private final InspectionType[] inspectionTypes;
     
	
	public static ProductTypeBuilder aProductType() {
		return new ProductTypeBuilder();
	}
	
	public ProductTypeBuilder() {
		this("*", "warnings", "instructions", "http://www.example.com", "no description", true, new ArrayList<InfoFieldBean>(), new HashSet<ProductType>(), null, new InspectionType[]{});
	}
	
	private ProductTypeBuilder(String name, String warnings, String instructions, String cautionsURL, String descriptionTemplate, boolean manufactureCertificate, Collection<InfoFieldBean> infoFields, Set<ProductType> subTypes, ProductTypeGroup group, InspectionType[] inspectionTypes) {
		super();
		this.name = name;
		this.warnings = warnings;
		this.instructions = instructions;
		this.cautionsURL = cautionsURL;
		this.descriptionTemplate = descriptionTemplate;
		this.manufactureCertificate = manufactureCertificate;
		this.infoFields = infoFields;
		this.subTypes = subTypes;
		this.group = group;
		this.inspectionTypes = inspectionTypes;
	}
	
	

	public ProductTypeBuilder named(String name) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, inspectionTypes);
	}
	
	public ProductTypeBuilder withFields(InfoFieldBean...infoFields) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, Arrays.asList(infoFields), subTypes, group, inspectionTypes);
	}
	
	public ProductTypeBuilder withSubTypes(ProductType...subTypes) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, new HashSet<ProductType>(Arrays.asList(subTypes)), group, inspectionTypes);
	}
	
	public ProductTypeBuilder withGroup(ProductTypeGroup group) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, inspectionTypes);
		
	}
	
	public ProductTypeBuilder withInspectionTypes(InspectionType...inspectionTypes) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group,inspectionTypes);
	}
	
	@Override
	public ProductType build() {
		ProductType productType = new ProductType();
		productType.setId(id);
		productType.setName(name);
		productType.setWarnings(warnings);
		productType.setInstructions(instructions);
		productType.setHasManufactureCertificate(manufactureCertificate);
		productType.setDescriptionTemplate(descriptionTemplate);
		productType.setCautionUrl(cautionsURL);
		
		for (InfoFieldBean infoField : infoFields) {
			infoField.setProductInfo(productType);
		}
		productType.setInfoFields(infoFields);
		productType.setSubTypes(subTypes);
		
		addInspectionTypes(productType);
		
		return productType;
	}

	private void addInspectionTypes(ProductType productType) {
		HashSet<AssociatedInspectionType> inspectionTypeSet = new HashSet<AssociatedInspectionType>();
		for (InspectionType inspectionType : inspectionTypes) {
			inspectionTypeSet.add(new AssociatedInspectionType(inspectionType, productType));
		}
		
		try {
			Field field = productType.getClass().getDeclaredField("inspectionTypes");
			field.setAccessible(true);
			field.set(productType, inspectionTypeSet);
			field.setAccessible(false);
		} catch (Exception e) {
			throw new Defect("something in the field changed", e);
		}
	}

	



}
