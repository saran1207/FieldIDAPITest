package com.n4systems.handlers.creator.safetynetwork;

import com.n4systems.handlers.creator.CreateHandler;
import com.n4systems.model.messages.MessageCommand;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.persistence.Transaction;

public interface ConnectionInvitationHandler extends CreateHandler {

	public void create(Transaction transaction);

	public ConnectionInvitationHandlerImpl withCommand(MessageCommand command);

	public ConnectionInvitationHandlerImpl from(InternalOrg localOrg);

	public ConnectionInvitationHandlerImpl to(InternalOrg remoteOrg);

	public ConnectionInvitationHandlerImpl personalizeBody(String personalizedBody);

	public boolean wasNotificationSent();

}