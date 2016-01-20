package com.n4systems.fieldid.actions.message;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.safetyNetwork.SafetyNetwork;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.creator.safetynetwork.CreateConnectionHandler;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigEntry;

@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SAFETY_NETWORK})
public class MessageCrud extends SafetyNetwork {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(MessageCrud.class);
	
	private Message message;
	private Pager<Message> page;
	
	public MessageCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		message = new Message();
	}


	@Override
	protected void loadMemberFields(Long uniqueId) {
		message = getLoaderFactory().createFilteredIdLoader(Message.class).setId(uniqueId).load();
	}
	
	private void testRequiredEntities(boolean existing) {
		if (message == null) {
			addActionErrorText("error.no_message");
			throw new MissingEntityException("no message created");
		}
		
		if (existing && message.isNew()) {
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
				new MessageSaver().update(message);
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
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		if (!message.isProcessed()) {
			try {
				processMessage();
				message.setProcessed(true);
				message.setModifiedBy(getUser());
				new MessageSaver().update(message);
				addFlashMessageText("message.connection_accepted");
				return SUCCESS;
			} catch (Exception e) {
				addActionErrorText("error.creating_connection");
				return ERROR;
			}
		} 
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doInvite() {
		message.setSubject("Invitation to connect on Field ID");
		
		return SUCCESS;
	}

	private void processMessage() throws Exception {
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		
		try {
			OrgConnectionSaver saver = new OrgConnectionSaver(getConfigContext().getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID));
			
			CreateConnectionHandler handler = new CreateConnectionHandler(saver, getDefaultNotifier(), getNonSecureLoaderFactory());
			handler.withMessage(message).create(transaction);
						
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		try {
			new MessageSaver().remove(message);
		} catch (Exception e) {
			return ERROR;
		}
		
		return SUCCESS;
	}

	public Message getMessage() {
		return message;
	}
	
	public Long getUnreadMessageCount() {
		return getLoaderFactory().createUnreadMessageCountLoader().load();
	}
	
	public Pager<Message> getPage() {
		return page;
	}
}
