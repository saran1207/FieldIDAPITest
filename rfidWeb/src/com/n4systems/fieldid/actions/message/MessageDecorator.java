package com.n4systems.fieldid.actions.message;

import java.util.Date;

import com.n4systems.model.messages.Message;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public class MessageDecorator {

	private Message message;
	

	public MessageDecorator(Message message) {
		super();
		this.message = message;
	}
	
	public String getBody() {
		return message.getBody();
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

	
	@RequiredStringValidator(message="", key="error.subject_is_required")
	@StringLengthFieldValidator(message="", key="error.subject_max_length", maxLength="1000")
	public void setSubject(String subject) {
		message.setSubject(subject);
	}

	public void setUnRead() {
		message.setUnRead();
	}

	public Message realMessage() {
		return message;
	}

}
