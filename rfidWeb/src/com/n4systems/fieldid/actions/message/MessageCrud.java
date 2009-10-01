package com.n4systems.fieldid.actions.message;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractPaginatedCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.model.Tenant;
import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageCommand;
import com.n4systems.model.messages.MessageCommandSaver;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class MessageCrud extends AbstractPaginatedCrud<Message> {
	private static final Logger logger = Logger.getLogger(MessageCrud.class);
	
	private MessageDecorator message;
	
	private Long messageId;
	
	public MessageCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		message = new MessageDecorator(new Message());
	}


	@Override
	protected void loadMemberFields(Long uniqueId) {
		message = new MessageDecorator(getLoaderFactory().createFilteredIdLoader(Message.class).setId(uniqueId).load());
	}
	
	
	private void testRequiredEntities(boolean existing) {
		if (message.message() == null) {
			addActionErrorText("error.no_message");
			throw new MissingEntityException("no message created");
		}
		
		if (existing && message.message().isNew()) {
			addActionErrorText("error.no_message");
			throw new MissingEntityException("message could not be loaded.");
		}
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		markMessageAsRead();
		return SUCCESS;
	}

	private void markMessageAsRead() {
		if (message.isUnread()) {
			message.setRead();
			try {
				new MessageSaver().save(message.message());
			} catch (Exception e) {
				logger.error("could not mark the message as read", e);
			}
		}
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
		Message realMessage = message.message();
		
		realMessage.setSender(getSessionUserOwner().getInternalOrg());
		realMessage.setReceiver(getPrimaryOrgForTenant(message.getReceiver()));
		
		realMessage.setCommand( new FilteredIdLoader<MessageCommand>(new OpenSecurityFilter(), MessageCommand.class).setId(messageId).load());
		
		
		try {
			new MessageSaver().save(realMessage);
		} catch (Exception e) {
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	

	private InternalOrg getPrimaryOrgForTenant(String receiver) {
		for (Tenant t : TenantCache.getInstance().findAllTenants()) {
			if (t.getName().equalsIgnoreCase(receiver)) {
				return TenantCache.getInstance().findPrimaryOrg(t.getId());
			}
		}
		return null;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		if (!message.message().getCommand().isProcessed()) {
			try {
				OrgConnectionSaver saver = new OrgConnectionSaver(ConfigContext.getCurrentContext().getLong(ConfigEntry.HOUSE_ACCOUNT_ID));
				
				OrgConnection connection = new OrgConnection();
				connection.setModifiedBy(getUser());
				CreateSafetyNetworkConnectionMessageCommand command = (CreateSafetyNetworkConnectionMessageCommand)message.message().getCommand();
				
				connection.setVendor((InternalOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(BaseOrg.class).setId(command.getVendorOrgId()).load());
				connection.setCustomer((InternalOrg)getNonSecureLoaderFactory().createNonSecureIdLoader(BaseOrg.class).setId(command.getCustomerOrgId()).load());
				saver.save(connection);
				
				command.setProcessed(true);
				new MessageCommandSaver().update(command);
				return SUCCESS;
			} catch (Exception e) {
				return ERROR;
			}
		} 
		
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		try {
			new MessageSaver().remove(message.message());
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@VisitorFieldValidator(message="")
	public MessageDecorator getMessage() {
		return message;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}


	
}
