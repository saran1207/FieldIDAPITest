package com.n4systems.model.activesession;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import com.n4systems.util.time.Clock;

@Entity
@Table(name="activesessions")
public class ActiveSession implements UnsecuredEntity, Saveable {
	
	@Id
	@Column(name="user_id")
	private Long userId;
	
	@PrimaryKeyJoinColumn(name="user_id")
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private User user;
	
	@Column
	private boolean active=true;
	
	@Column(nullable=false, length=64)
	private String sessionId;
	
	@Column(nullable=false)
	protected Date lastTouched;
	
	
	@Column(nullable=false)
	private Date dateCreated = new Date();

	public ActiveSession() {
	}
	
	public ActiveSession(User user, String sessionId) {
		super();
		setUser(user);
		this.sessionId = sessionId;
		updateLastTouched();
	}
	
	
	@PrePersist
	@PreUpdate
	private void updateLastTouched() {
		lastTouched = new Date();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.userId = user.getId();
		
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public Long getIdentifier() {
		return userId;
	}

	@Override
	public boolean isNew() {
		return true;
	}

	public Date getLastTouched() {
		return lastTouched;
	}

	public boolean isExpired(int timeoutInMinutes, Clock clock) {
		return timeoutInMinutes <= DateHelper.getMinutesDelta(lastTouched, clock.currentTime());
	}
	
	public boolean isForSystemUser() {
		return user.isSystem();
	}

	public boolean isForNonSystemUser() {
		return !isForSystemUser();
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void setActive(boolean flag){
		
		active= (isForSystemUser()) ? true : flag;
	}
	
	/** Nulls the modified field.  Will force Hibernate to save on merge. */
	public void touch() {
		lastTouched = null;
	}

	public Date getDateCreated() {
		return dateCreated;
	}
	
	
	
	
}
