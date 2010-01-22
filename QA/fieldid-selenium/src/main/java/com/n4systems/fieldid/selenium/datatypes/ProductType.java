package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class ProductType {
	String group;
	String name;
	String warnings;
	String instructions;
	String cautionsURL;
	boolean hasManufacturerCertificate = false;
	String manufacturerCertificateText;
	String productDescriptionTemplate;
	String uploadImageFileName;
	List<Attribute> attributes = new ArrayList<Attribute>();
	List<Attachment> attachments = new ArrayList<Attachment>();
	
	public ProductType(String name) {
		this.name = name;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getName() {
		return name;
	}
	
	public String getWarnings() {
		return warnings;
	}
	
	public String getInstructions() {
		return instructions;
	}
	
	public String getCautionsURL() {
		return cautionsURL;
	}
	
	public boolean getHasManufacturerCertificate() {
		return hasManufacturerCertificate;
	}
	
	public String getManufacturerCertificateText() {
		return manufacturerCertificateText;
	}
	
	public String getProductDescriptionTemplate() {
		return productDescriptionTemplate;
	}
	
	public String getUploadImageFileName() {
		return uploadImageFileName;
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setGroup(String s) {
		this.group = s;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void setWarnings(String s) {
		this.warnings =  s;
	}
	
	public void setInstructions(String s) {
		instructions = s;
	}
	
	public void setCautionsURL(String s) {
		cautionsURL = s;
	}
	
	public void setHasManufacturerCertificate(boolean b) {
		hasManufacturerCertificate = b;
	}
	
	public void setManufacturerCertificateText(String s) {
		manufacturerCertificateText = s;
	}
	
	public void setProductDescriptionTemplate(String s) {
		productDescriptionTemplate = s;
	}
	
	public void setUploadImageFileName(String s) {
		uploadImageFileName = s;
	}
	
	public void addAttributes(Attribute a) {
		attributes.add(a);
	}
	
	public void addAttachments(Attachment a) {
		attachments.add(a);
	}

	public void removeAttributes(Attribute a) {
		attributes.remove(a);
	}
	
	public void removeAttachments(Attachment a) {
		attachments.remove(a);
	}
}
