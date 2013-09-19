package com.n4systems.model.activesession;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import com.n4systems.util.time.Clock;

import javax.persistence.*;
import java.util.Date;

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
	
	@Column
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
    public Object getEntityId() {
        return userId;
    }

    @Override
    public boolean isTranslated() {
        return false;
    }

    @Override
    public void setTranslated(boolean translated) {
        ; // do nothing   N/A
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
