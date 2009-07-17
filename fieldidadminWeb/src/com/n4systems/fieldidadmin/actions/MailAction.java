package com.n4systems.fieldidadmin.actions;

import com.n4systems.ejb.MailManager;
import com.n4systems.util.mail.MailMessage;

public class MailAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	private MailManager mailManager;
	
	private String toAddress;
	private String subject;
	private String body;
	private String attachmentPath;
	private boolean html;
	
	public String doInput() {
		html = true;
		return SUCCESS;
	}
	
	public String doSend() {
		try {
			MailMessage message = new MailMessage();
			message.setSubject(subject);
			message.setBody(body);
			message.getToAddresses().add(toAddress);
			
			if(!html) {
				message.setContentType(MailMessage.ContentType.PLAIN);
			}
			
			if(attachmentPath != null && attachmentPath.length() > 0) {
				message.addAttachment(attachmentPath);
			}
			mailManager.sendMessage(message);
			
		} catch(Exception e) {
			addActionError("Send Failed: " + e.getMessage());
			e.printStackTrace(System.err);
			return ERROR;
		}
		
		addActionMessage("Success");
		return SUCCESS;
	}

	public String getToAddress() {
		return toAddress;
	}


	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

}
