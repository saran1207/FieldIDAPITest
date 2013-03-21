package com.n4systems.ejb.impl;

import com.n4systems.model.Asset;
import com.n4systems.model.EventType;
import com.n4systems.model.Project;
import com.n4systems.model.user.Assignable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class EventScheduleBundle {	
	private final Asset asset;
	private final EventType type;
	private final Project job;
	private final Date scheduledDate;
    private final Assignable assginee;
	
	public EventScheduleBundle(Asset asset, EventType type, Project job, Date scheduledDate) {
        this(asset, type, job, scheduledDate, null);
    }

    public EventScheduleBundle(Asset asset, EventType type, Project job, Date scheduledDate, Assignable assignee) {
        this.asset = asset;
        this.type = type;
        this.job = job;
        this.scheduledDate = scheduledDate;
        this.assginee = assignee;

        guard();
    }

	private void guard() {
		if (asset == null) {
			throw new NullPointerException("asset");
		}
		
		if (type == null) {
			throw new NullPointerException("type");
		}
		
		if (scheduledDate == null) {
			throw new NullPointerException("scheduleDate");
		}
	}
	
	public Asset getAsset() {
		return asset;
	}

	public EventType getType() {
		return type;
	}

	public Project getJob() {
		return job;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

    public Assignable getAssginee() {
        return assginee;
    }

    @Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
