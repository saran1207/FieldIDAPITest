package com.n4systems.model.downloadlink;

public enum ContentType {
	ZIP		("zip", "application/zip"), 
	PDF		("pdf", "application/pdf"), 
//	EXCEL	("xls", "application/vnd.ms-excel"),//This is the older Excel format. Note that it is a different MIME type.
	EXCEL	("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	CSV		("csv", "text/csv");
	
	
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
