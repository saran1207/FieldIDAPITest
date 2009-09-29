package com.n4systems.fieldid.actions.message;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractPaginatedCrud;
import com.n4systems.model.messages.Message;

public class MessageCrud extends AbstractPaginatedCrud<Message> {

	public MessageCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}


	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	
	private void testRequiredEntities(boolean existing) {
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		page = getLoaderFactory().createPaginatedMessageLoader().setPage(getCurrentPage()).load();
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		return SUCCESS;
	}


	
}
