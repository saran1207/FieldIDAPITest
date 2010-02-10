package com.n4systems.fieldid.certificate.model;

import java.io.File;

public class InspectionImage {

	private final File path;
	private final String comment;
	
	public InspectionImage(File path, String comment) {
		super();
		this.path = path;
		this.comment = comment;
	}

	public File getPath() {
		return path;
	}

	public String getComment() {
		return comment;
	}
	
	
}
