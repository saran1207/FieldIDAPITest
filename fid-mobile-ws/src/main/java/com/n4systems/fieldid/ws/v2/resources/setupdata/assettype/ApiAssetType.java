package com.n4systems.fieldid.ws.v2.resources.setupdata.assettype;

import com.n4systems.fieldid.ws.v2.resources.setupdata.assettype.attributes.ApiAttribute;
import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiAssetType extends ApiReadonlyModel {
	private String name;
	private String warnings;
	private String instructions;
	private String cautionUrl;
	private String descriptionTemplate;
    private boolean hasProcedures;
	private String identifierFormat;
	private String identifierLabel;
	private boolean identifierOverridden;
	private byte[] image;
	private ApiAssetTypeGroup group;
	private boolean linkable;
	private List<ApiAssetTypeSchedule> schedules = new ArrayList<>();
	private List<Long> allowedEventTypes = new ArrayList<>();
	private List<ApiAttribute> attributes = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getCautionUrl() {
		return cautionUrl;
	}

	public void setCautionUrl(String cautionUrl) {
		this.cautionUrl = cautionUrl;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getDescriptionTemplate() {
		return descriptionTemplate;
	}

	public void setDescriptionTemplate(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate;
	}

    public boolean getHasProcedures() {
        return hasProcedures;
    }

    public void setHasProcedures(boolean hasProcedures) {
        this.hasProcedures = hasProcedures;
    }

	public String getIdentifierFormat() {
		return identifierFormat;
	}

	public void setIdentifierFormat(String identifierFormat) {
		this.identifierFormat = identifierFormat;
	}

	public String getIdentifierLabel() {
		return identifierLabel;
	}

	public void setIdentifierLabel(String identifierLabel) {
		this.identifierLabel = identifierLabel;
	}

	public boolean isIdentifierOverridden() {
		return identifierOverridden;
	}

	public void setIdentifierOverridden(boolean identifierOverridden) {
		this.identifierOverridden = identifierOverridden;
	}

	public ApiAssetTypeGroup getGroup() {
		return group;
	}

	public void setGroup(ApiAssetTypeGroup group) {
		this.group = group;
	}

	public List<ApiAssetTypeSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ApiAssetTypeSchedule> schedules) {
		this.schedules = schedules;
	}

	public List<Long> getAllowedEventTypes() {
		return allowedEventTypes;
	}

	public void setAllowedEventTypes(List<Long> allowedEventTypes) {
		this.allowedEventTypes = allowedEventTypes;
	}

	public List<ApiAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ApiAttribute> attributes) {
		this.attributes = attributes;
	}
	
	public boolean isLinkable() {
		return linkable;
	}

	public void setLinkable(boolean linkable) {
		this.linkable = linkable;
	}	
}
