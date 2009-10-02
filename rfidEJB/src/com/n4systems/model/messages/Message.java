package com.n4systems.model.messages;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.parents.EntityWithOwner;

@Entity
@Table(name="messages")
public class Message extends EntityWithOwner {

	private boolean unread = true;
	
	@Column(nullable = false, length=255)
	private String sender;
	
	@Column(nullable = false, length=255)
	private String receiver;
	
	@Column(nullable = false, length=1000)
	private String subject;
	
	@Column(nullable = false)
	private String body;
	
	@OneToOne(optional=false, cascade=CascadeType.REMOVE)
	private MessageCommand command;
	
	public Message() {
	}

	public boolean isRead() {
		return !unread;
	}

	public void setRead() {
		unread = false;
	}
	
	public boolean isUnRead() {
		return unread;
	}
	
	public void setUnRead() {
		unread = true;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(InternalOrg sender) {
		this.sender = sender.getName();
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(InternalOrg receiver) {
		this.receiver = receiver.getName();
		setOwner(receiver);
		setTenant(receiver.getTenant());
	}

	public Date getSentTime() {
		return getCreated();
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

	public MessageCommand getCommand() {
		return command;
	}

	public void setCommand(MessageCommand command) {
		this.command = command;
	}

}
