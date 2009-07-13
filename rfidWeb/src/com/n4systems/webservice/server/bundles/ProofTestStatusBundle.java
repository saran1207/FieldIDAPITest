package com.n4systems.webservice.server.bundles;


public class ProofTestStatusBundle extends WebServiceStatusBundle {
	private static final long serialVersionUID = 1L;
	
	private String fileName;
	
	public ProofTestStatusBundle(String fileName, WebServiceStatus status, String message) {
		super(status, message);
		this.fileName = fileName;
	}
	
	public ProofTestStatusBundle(String fileName, WebServiceStatus status) {
		this(fileName, status, null);
	}
	
	public ProofTestStatusBundle(String fileName) {
		this(fileName, null, null);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
