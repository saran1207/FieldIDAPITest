package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.Contact;
import com.n4systems.persistence.Transaction;

public class CustomerContactMapBuilder extends AbstractMapBuilder<Contact> {

	public CustomerContactMapBuilder() {
		super(ReportField.CUSTOMER_CONTACT_NAME, ReportField.CUSTOMER_CONTACT_EMAIL);
	}

	@Override
	protected void setAllFields(Contact entity, Transaction transaction) {
		setField(ReportField.CUSTOMER_CONTACT_NAME, entity.getName());
		setField(ReportField.CUSTOMER_CONTACT_EMAIL, entity.getEmail());
	}
	
}
