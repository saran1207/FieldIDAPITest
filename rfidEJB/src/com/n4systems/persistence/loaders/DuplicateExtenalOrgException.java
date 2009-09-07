package com.n4systems.persistence.loaders;

import java.util.List;

import com.n4systems.model.orgs.ExternalOrg;

public class DuplicateExtenalOrgException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private List<? extends ExternalOrg> orgs;

	public DuplicateExtenalOrgException(List<? extends ExternalOrg> orgs) {
		this.orgs = orgs;
	}

	@Override
	public String getMessage() {
		StringBuilder message = new StringBuilder("Matched more then 1 ");
		
		if (orgs.isEmpty()) {
			message.append("External Organization");
		} else {
			if (orgs.get(0).isCustomer()) {
				message.append("Customer");
			} else {
				message.append("Division");
			}
		}
		
		for (ExternalOrg customer: orgs) {
			message.append(": [").append(customer.toString()).append("]");
		}
		
		return message.toString();
	}
	
}
