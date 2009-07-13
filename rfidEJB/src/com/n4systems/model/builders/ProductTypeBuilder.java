package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.model.ProductType;

public class ProductTypeBuilder extends BaseBuilder<ProductType> {

	private final String name;
	private final String warnings;
    private final String instructions;
    private final String cautionsURL;
    private final boolean manufactureCertificate;
    private final String descriptionTemplate;
    private final Collection<InfoFieldBean> infoFields;
    private final Set<ProductType> subTypes;
     
	
	public static ProductTypeBuilder aProductType() {
		return new ProductTypeBuilder();
	}
	
	public ProductTypeBuilder() {
		this("*", "warnings", "instructions", "http://www.example.com", "no description", true, new ArrayList<InfoFieldBean>(), new HashSet<ProductType>());
	}
	
	public ProductTypeBuilder(String name, String warnings, String instructions, String cautionsURL, String descriptionTemplate, boolean manufactureCertificate, Collection<InfoFieldBean> infoFields, Set<ProductType> subTypes) {
		super();
		this.name = name;
		this.warnings = warnings;
		this.instructions = instructions;
		this.cautionsURL = cautionsURL;
		this.descriptionTemplate = descriptionTemplate;
		this.manufactureCertificate = manufactureCertificate;
		this.infoFields = infoFields;
		this.subTypes = subTypes;
	}
	
	

	public ProductTypeBuilder named(String name) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes);
	}
	
	public ProductTypeBuilder withFields(InfoFieldBean...infoFields) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, Arrays.asList(infoFields), subTypes);
	}
	
	public ProductTypeBuilder withSubTypes(ProductType...subTypes) {
		return new ProductTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, new HashSet<ProductType>(Arrays.asList(subTypes)));
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
		return productType;
	}

}
