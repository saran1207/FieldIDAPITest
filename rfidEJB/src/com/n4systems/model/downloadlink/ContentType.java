package com.n4systems.model.downloadlink;

public enum ContentType {
	ZIP		("zip", "application/zip"), 
	PDF		("pdf", "application/pdf"), 
	EXCEL	("xls", "application/vnd.ms-excel"),
	CSV		("csv", "application/vnd.ms-excel");
	
	private final String extension;
	private final String mimeType;
	
	ContentType(String extension, String mimeType) {
		this.extension = extension;
		this.mimeType = mimeType;
	}

	public String getExtension() {
		return extension;
	}

	public String getMimeType() {
		return mimeType;
	}
	
	public String prepareFileName(String name) {
		return String.format("%s.%s", name, extension);
	}
}
