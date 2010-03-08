package com.n4systems.fieldidadmin.actions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.n4systems.ejb.MailManagerImpl;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.MailMessage.MessageType;

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
			MessageType contentType = html ? MailMessage.MessageType.HTML : MailMessage.MessageType.PLAIN;
			MailMessage message = new MailMessage(contentType, ConfigContext.getCurrentContext());
			message.setSubject(subject);
			message.setBody(body);
			message.getToAddresses().add(toAddress);
			
			if(attachmentPath != null && attachmentPath.length() > 0) {
				File attachment = new File(attachmentPath);
				
				message.getAttachments().put(attachment.getName(), getFileData(attachment));
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

	private byte[] getFileData(File summaryFileOutput) {
		byte[] data = null;
		InputStream in = null;
		try {
			data = IOUtils.toByteArray(in);
		} catch (IOException e) {
			
		} finally {
			IOUtils.closeQuietly(in);
		}
		return data;
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
