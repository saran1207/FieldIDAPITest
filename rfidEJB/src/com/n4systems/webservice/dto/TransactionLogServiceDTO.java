package com.n4systems.webservice.dto;

import com.n4systems.model.TransactionActionType;

public class TransactionLogServiceDTO extends AbstractBaseServiceDTO {
	
	TransactionActionType action;

	String referenceClass;
	
	Long referenceId;

	public TransactionActionType getAction() {
		return action;
	}

	public void setAction(TransactionActionType action) {
		this.action = action;
	}

	public String getReferenceClass() {
		return referenceClass;
	}

	public void setReferenceClass(String referenceClass) {
		this.referenceClass = referenceClass;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

}
