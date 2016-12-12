package com.n4systems.model.messages;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.parents.EntityWithOwner;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="messages")
public class Message extends EntityWithOwner {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sender_id")
	private PrimaryOrg sender;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "recipient_id")
	private PrimaryOrg recipient;
	
	@Column(nullable = false, length=1000)
	private String subject;
	
	@Column(nullable = false)
	private String body;
	
	private boolean unread = true;
	
	private boolean vendorConnection;
	
	private boolean processed;
	
	public Message() {
	}

	public boolean isRead() {
		return !unread;
	}

	public void setRead() {
		unread = false;
	}
	
	public boolean isUnread() {
		return unread;
	}
	
	public void setUnread() {
		unread = true;
	}

	public PrimaryOrg getSender() {
		return sender;
	}

	public void setSender(PrimaryOrg sender) {
		this.sender = sender;
	}

	public PrimaryOrg getRecipient() {
		return recipient;
	}

	public void setRecipient(PrimaryOrg receiver) {
		this.recipient = receiver;
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
	
	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public boolean isVendorConnection() {
		return vendorConnection;
	}

	public void setVendorConnection(boolean vendorConnection) {
		this.vendorConnection = vendorConnection;
	}

}
