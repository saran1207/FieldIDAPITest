package com.n4systems.exporting.beanutils;

@SuppressWarnings("serial")
public class InvalidTitleException extends MarshalingException {
	private final String title;
	
	public InvalidTitleException(String title) {
		super(title);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
}
