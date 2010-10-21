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
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;

public class AssetTypeBuilder extends BaseBuilder<AssetType> {

	private final String name;
	private final String warnings;
    private final String instructions;
    private final String cautionsURL;
    private final boolean manufactureCertificate;
    private final String descriptionTemplate;
    private final AssetTypeGroup group;
    private final Collection<InfoFieldBean> infoFields;
    private final Set<AssetType> subTypes;
	private final InspectionType[] inspectionTypes;
	
	public static AssetTypeBuilder anAssetType() {
		return new AssetTypeBuilder();
	}
	
	public AssetTypeBuilder() {
		this("*", "warnings", "instructions", "http://www.example.com", "no description", true, new ArrayList<InfoFieldBean>(), new HashSet<AssetType>(), null, new InspectionType[]{});
	}
	
	private AssetTypeBuilder(String name, String warnings, String instructions, String cautionsURL, String descriptionTemplate, boolean manufactureCertificate, Collection<InfoFieldBean> infoFields, Set<AssetType> subTypes, AssetTypeGroup group, InspectionType[] inspectionTypes) {
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

	public AssetTypeBuilder named(String name) {
		return new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, inspectionTypes);
	}
	
	public AssetTypeBuilder withFields(InfoFieldBean...infoFields) {
		return new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, Arrays.asList(infoFields), subTypes, group, inspectionTypes);
	}
	
	public AssetTypeBuilder withSubTypes(AssetType...subTypes) {
		return new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, new HashSet<AssetType>(Arrays.asList(subTypes)), group, inspectionTypes);
	}
	
	public AssetTypeBuilder withGroup(AssetTypeGroup group) {
		return new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, inspectionTypes);
	}
	
	public AssetTypeBuilder withInspectionTypes(InspectionType...inspectionTypes) {
		return new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group,inspectionTypes);
	}
	
	@Override
	public AssetType createObject() {
		AssetType assetType = new AssetType();
		assetType.setId(id);
		assetType.setName(name);
		assetType.setWarnings(warnings);
		assetType.setInstructions(instructions);
		assetType.setHasManufactureCertificate(manufactureCertificate);
		assetType.setDescriptionTemplate(descriptionTemplate);
		assetType.setCautionUrl(cautionsURL);
		
		for (InfoFieldBean infoField : infoFields) {
			infoField.setAssetInfo(assetType);
		}
		assetType.setInfoFields(infoFields);
		assetType.setSubTypes(subTypes);
		
		addInspectionTypes(assetType);
		
		return assetType;
	}

	private void addInspectionTypes(AssetType assetType) {
		HashSet<AssociatedInspectionType> inspectionTypeSet = new HashSet<AssociatedInspectionType>();
		for (InspectionType inspectionType : inspectionTypes) {
			inspectionTypeSet.add(new AssociatedInspectionType(inspectionType, assetType));
		}
		
		try {
			Field field = assetType.getClass().getDeclaredField("inspectionTypes");
			field.setAccessible(true);
			field.set(assetType, inspectionTypeSet);
			field.setAccessible(false);
		} catch (Exception e) {
			throw new Defect("something in the field changed", e);
		}
	}

}
