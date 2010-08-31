package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.Contact;
import com.n4systems.persistence.Transaction;

public class DivisionContactMapBuilder extends AbstractMapBuilder<Contact> {

	public DivisionContactMapBuilder() {
		super(ReportField.DIVISION_CONTACT_NAME, ReportField.DIVISION_CONTACT_EMAIL);
	}
	
	@Override
	protected void setAllFields(Contact entity, Transaction transaction) {
		setField(ReportField.DIVISION_CONTACT_NAME, 	entity.getName());
		setField(ReportField.DIVISION_CONTACT_EMAIL,	entity.getEmail());
	}

}
