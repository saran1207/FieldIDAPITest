package com.n4systems.fieldid.ws.v1.resources.eventtype;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.eventtype.attributes.ApiAttribute;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiAssetType extends ApiReadonlyModel {
	private String name;
	private String warnings;
	private String instructions;
	private String cautionUrl;
	private String descriptionTemplate;
	private String identifierFormat;
	private String identifierLabel;
	private boolean identifierOverridden;
	private byte[] image;
	private ApiAssetTypeGroup group;
	private List<ApiAssetTypeSchedule> schedules = new ArrayList<ApiAssetTypeSchedule>();
	private List<Long> allowedEventTypes = new ArrayList<Long>();
	private List<ApiAttribute> attributes = new ArrayList<ApiAttribute>();

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
}
