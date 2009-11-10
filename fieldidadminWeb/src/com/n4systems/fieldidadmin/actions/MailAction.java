package com.n4systems.fieldidadmin.actions;

import com.n4systems.ejb.MailManagerImpl;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.MailMessage.ContentType;

public class MailAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	
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
			ContentType contentType = html ? MailMessage.ContentType.HTML : MailMessage.ContentType.PLAIN;
			MailMessage message = new MailMessage(contentType, subject, body);
			message.getToAddresses().add(toAddress);
			
			
			
			if(attachmentPath != null && attachmentPath.length() > 0) {
				message.addAttachment(attachmentPath);
			}
			new MailManagerImpl().sendMessage(message);
			
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

	
}
