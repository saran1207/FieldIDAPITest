package com.n4systems.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

/**
 * A simple embeddable entity representing a contact.
 */
@Embeddable
public class Contact implements Serializable, SecurityEnhanced<Contact> {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	
	public Contact() {}
	
	public Contact(String name, String email) {
		this.name = name;
		this.email = email;
	}

	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public Contact enhance(SecurityLevel level) {
		Contact enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		return enhanced;
	}
}
