package com.n4systems.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * A simple embeddable entity representing a contact.
 */
@Embeddable
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	
	public Contact() {}
	
	public Contact(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
