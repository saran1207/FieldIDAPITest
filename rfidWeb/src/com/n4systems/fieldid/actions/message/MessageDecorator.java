package com.n4systems.fieldid.actions.message;

import java.util.Date;

import com.n4systems.model.messages.Message;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public class MessageDecorator {

	private Message message;
	
	private String receiver;

	
	public MessageDecorator(Message message) {
		super();
		this.message = message;
		receiver = message.getReceiver();
	}
	
	public String getBody() {
		return message.getBody();
	}

	
	public String getReceiver() {
		return receiver;
	}


	public Date getSentTime() {
		return message.getSentTime();
	}

	
	public String getSubject() {
		return message.getSubject();
	}

	public boolean isNew() {
		return message.isNew();
	}

	public boolean isRead() {
		return message.isRead();
	}

	public boolean isUnread() {
		return message.isUnRead();
	}

	@RequiredStringValidator(message="", key="error.body_is_required")
	public void setBody(String body) {
		message.setBody(body);
	}

	public void setRead() {
		message.setRead();
	}

	@RequiredFieldValidator(message="", key="error.recipient_is_required")
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	
	@RequiredStringValidator(message="", key="error.subject_is_required")
	@StringLengthFieldValidator(message="", key="error.subject_max_length", maxLength="1000")
	public void setSubject(String subject) {
		message.setSubject(subject);
	}

	public void setUnRead() {
		message.setUnRead();
	}

	public Message message() {
		return message;
	}

}
