package com.n4systems.fieldid.datatypes;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductType {

	String group = null;
	String name = null;
	String warnings = null;
	String instructions = null;
	String cautionsURL = null;
	boolean hasManufacturerCertificate = false;
	String manufacturerCertificateText = null;
	String productDescriptionTemplate = null;
	String uploadImage = null;
	ProductAttribute[] attributes = null;
	ArrayList<String> attachments = new ArrayList<String>();
	
	public ProductType(String name) {
		this.name = name;
	}

	public ProductType(String group, String name, String warnings, String instructions, String cautionsURL,
			boolean hasManufacturerCertificate, String manufacturerCertificateText, String productDescriptionTemplate,
			String uploadImage, ProductAttribute[] attributes, String[] attachments) {
		this.group = group;
		this.name = name;
		this.warnings = warnings;
		this.instructions = instructions;
		this.cautionsURL = cautionsURL;
		this.hasManufacturerCertificate = hasManufacturerCertificate;
		this.manufacturerCertificateText = manufacturerCertificateText;
		this.productDescriptionTemplate = productDescriptionTemplate;
		this.uploadImage = uploadImage;
		this.attributes = attributes;
		this.attachments.addAll(Arrays.asList(attachments));
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}
	
	public String getWarnings() {
		return warnings;
	}
	
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	public String getInstructions() {
		return instructions;
	}
	
	public void setCautionsURL(String cautionsURL) {
		this.cautionsURL = cautionsURL;
	}
	
	public String getCautionsURL() {
		return cautionsURL;
	}
	
	public void setHasManufacturerCertificate(boolean hasManufacturerCertificate) {
		this.hasManufacturerCertificate = hasManufacturerCertificate;
	}
	
	public boolean getHasManufacturerCertificate() {
		return hasManufacturerCertificate;
	}
	
	public void setManufacturerCertificateText(String manufacturerCertificateText) {
		this.manufacturerCertificateText = manufacturerCertificateText;
	}
	
	public String getManufacturerCertificateText() {
		return manufacturerCertificateText;
	}
	
	public void setProductDescriptionTemplate(String productDescriptionTemplate) {
		this.productDescriptionTemplate = productDescriptionTemplate;
	}
	
	public String getProductDescriptionTemplate() {
		return productDescriptionTemplate;
	}
	
	public void setUploadImage(String uploadImage) {
		this.uploadImage = uploadImage;
	}
	
	public String getUploadImage() {
		return uploadImage;
	}

	public void setAttributes(ProductAttribute[] attributes) {
		this.attributes = attributes;
	}
	
	public ProductAttribute[] getAttributes() {
		return attributes;
	}

	public ProductAttribute getAttributes(int index) {
		return attributes[index];
	}

	public void setAttachments(String[] attachments) {
		this.attachments.addAll(Arrays.asList(attachments));
	}
	
	public ArrayList<String> getAttachments() {
		return attachments;
	}
	
	public String getAttachments(int index) {
		return attachments.get(index);
	}
}
