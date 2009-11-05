package com.n4systems.fieldid.actions.safetyNetwork;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.TemplateMailMessage;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class SendInvitationAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SendInvitationAction.class);

	private String email;
	private String subject;
	private String body;
	
	public SendInvitationAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doShow() {
		subject = getInternalOrg().getName()+" "+getText("label.default_subject");
		body = getText("label.default_body");
		
		return SUCCESS;
	}
	
	public String doSend() {
		TemplateMailMessage invitationMessage = new TemplateMailMessage(subject, "invitation.ftl");
		
		invitationMessage.getToAddresses().add(email);
		invitationMessage.getTemplateMap().put("customMessage", body);
		invitationMessage.getTemplateMap().put("senderName", getInternalOrg().getName());
		invitationMessage.getTemplateMap().put("signupURL", createActionURI("public/signUpPackages.action"));

		boolean success = true;
		
		try {
			ServiceLocator.getMailManager().sendMessage(invitationMessage);
		
		} catch (MessagingException e) {
			logger.error("Could not send invitation message", e);
			addFlashErrorText("error.problem_sending_email");
			success=false;
		} catch (RuntimeException e) {
			logger.error("Could not send invitation message", e);
			addFlashErrorText("error.problem_sending_email");
			success=false;
		}
		
		if (success) {
			addFlashMessageText("label.invitation_sent");
		}
		
		return SUCCESS;
	}

	public String getEmail() {
		return email;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, key="label.email_required", message="")
	@EmailValidator(key="label.not_valid_email", message="")
	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, key="label.subject_required", message="")
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	
}
