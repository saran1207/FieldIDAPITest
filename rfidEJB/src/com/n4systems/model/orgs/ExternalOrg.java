package com.n4systems.model.orgs;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Contact;


@SuppressWarnings("serial")
@MappedSuperclass
abstract public class ExternalOrg extends BaseOrg {
	
	private String code;
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name="name", column = @Column(name="contactname")),
		@AttributeOverride(name="email", column = @Column(name="contactemail"))
	})
	private Contact contact = new Contact();
	
	public ExternalOrg() {}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
}
