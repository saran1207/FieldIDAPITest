package com.n4systems.webservice.server.bundles;

import java.io.Serializable;

public class ProofTestBundle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private byte[] fileData;
	private String fileName; 
	private String fileType; 
	private boolean createProduct = false;
	private boolean createCustomer = false;
	
	public ProofTestBundle() {}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public boolean isCreateProduct() {
		return createProduct;
	}

	public void setCreateProduct(boolean addProduct) {
		this.createProduct = addProduct;
	}

	public boolean isCreateCustomer() {
		return createCustomer;
	}

	public void setCreateCustomer(boolean createCustomer) {
		this.createCustomer = createCustomer;
	}
	
	
}
