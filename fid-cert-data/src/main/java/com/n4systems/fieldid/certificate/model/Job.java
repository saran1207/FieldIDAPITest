package com.n4systems.fieldid.certificate.model;

public class Job {

	private final String identifier;
	private final String title;
	
	
	public Job(String identifier, String title) {
		super();
		validate(identifier, title);
		
		this.identifier = identifier;
		this.title = title;
	}


	private void validate(String identifier, String title) {
		if (identifier == null) {
			throw new IllegalArgumentException("identifier can not be null");
		}
		if (title == null) {
			throw new IllegalArgumentException("title can not be null");
		}
	}


	public String getIdentifier() {
		return identifier;
	}


	public String getTitle() {
		return title;
	}
	
	
}
