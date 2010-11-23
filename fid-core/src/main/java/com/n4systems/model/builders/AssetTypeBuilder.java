package com.n4systems.model.builders;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.exceptions.Defect;
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
	private final EventType[] eventTypes;
    private final Tenant tenant;
	
	public static AssetTypeBuilder anAssetType() {
		return new AssetTypeBuilder();
	}
	
	public AssetTypeBuilder() {
		this("*", "warnings", "instructions", "http://www.example.com", "no description", true, new ArrayList<InfoFieldBean>(), new HashSet<AssetType>(), null, new EventType[]{}, null);
	}
	
	private AssetTypeBuilder(String name, String warnings, String instructions, String cautionsURL, String descriptionTemplate, boolean manufactureCertificate, Collection<InfoFieldBean> infoFields, Set<AssetType> subTypes, AssetTypeGroup group, EventType[] eventTypes, Tenant tenant) {
		this.name = name;
		this.warnings = warnings;
		this.instructions = instructions;
		this.cautionsURL = cautionsURL;
		this.descriptionTemplate = descriptionTemplate;
		this.manufactureCertificate = manufactureCertificate;
		this.infoFields = infoFields;
		this.subTypes = subTypes;
		this.group = group;
		this.eventTypes = eventTypes;
        this.tenant = tenant;
	}

	public AssetTypeBuilder named(String name) {
		return makeBuilder(new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, eventTypes, tenant));
	}
	
	public AssetTypeBuilder withFields(InfoFieldBean...infoFields) {
		return makeBuilder(new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, Arrays.asList(infoFields), subTypes, group, eventTypes, tenant));
	}
	
	public AssetTypeBuilder withSubTypes(AssetType...subTypes) {
		return makeBuilder(new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, new HashSet<AssetType>(Arrays.asList(subTypes)), group, eventTypes, tenant));
	}
	
	public AssetTypeBuilder withGroup(AssetTypeGroup group) {
		return makeBuilder(new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, eventTypes, tenant));
	}
	
	public AssetTypeBuilder withEventTypes(EventType... eventTypes) {
		return makeBuilder(new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, eventTypes, tenant));
	}

	public AssetTypeBuilder forTenant(Tenant tenant) {
		return makeBuilder(new AssetTypeBuilder(name, warnings, instructions, cautionsURL, descriptionTemplate, manufactureCertificate, infoFields, subTypes, group, eventTypes, tenant));
	}
	
	@Override
	public AssetType createObject() {
		AssetType assetType = new AssetType();
		assetType.setId(getId());
		assetType.setName(name);
		assetType.setWarnings(warnings);
		assetType.setInstructions(instructions);
		assetType.setHasManufactureCertificate(manufactureCertificate);
		assetType.setDescriptionTemplate(descriptionTemplate);
		assetType.setCautionUrl(cautionsURL);
        assetType.setTenant(tenant);
		
		for (InfoFieldBean infoField : infoFields) {
			infoField.setAssetInfo(assetType);
		}
		assetType.setInfoFields(infoFields);
		assetType.setSubTypes(subTypes);
		
		addEventTypes(assetType);
		
		return assetType;
	}

	private void addEventTypes(AssetType assetType) {
		HashSet<AssociatedEventType> eventTypeSet = new HashSet<AssociatedEventType>();
		for (EventType eventType : eventTypes) {
			eventTypeSet.add(new AssociatedEventType(eventType, assetType));
		}
		
		try {
			Field field = assetType.getClass().getDeclaredField("eventTypes");
			field.setAccessible(true);
			field.set(assetType, eventTypeSet);
			field.setAccessible(false);
		} catch (Exception e) {
			throw new Defect("something in the field changed", e);
		}
	}

}
