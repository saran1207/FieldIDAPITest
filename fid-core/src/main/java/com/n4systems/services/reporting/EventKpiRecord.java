package com.n4systems.services.reporting;

import com.n4systems.model.orgs.BaseOrg;

import java.io.Serializable;

public class EventKpiRecord implements Serializable {
	
	private Long completed = 0L;
	
	private Long inProgress = 0L;

    private Long closed = 0L;
	
	private Long scheduled = 0L;
	
	private Long failed = 0L;
	
	private BaseOrg customer;

	public Long getCompleted() {
		return completed;
	}

	public void setCompleted(Long completed) {
		this.completed = completed;
	}

	public Long getInProgress() {
		return inProgress;
	}

	public void setInProgress(Long inProgress) {
		this.inProgress = inProgress;
	}

	public Long getScheduled() {
		return scheduled;
	}

	public void setScheduled(Long scheduled) {
		this.scheduled = scheduled;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public BaseOrg getCustomer() {
		return customer;
	}

	public void setCustomer(BaseOrg customer) {
		this.customer = customer;
	}

	public Long getTotalScheduledEvents() {
		return completed + inProgress + scheduled;
	}

    public Long getClosed() {
        return closed;
    }

    public void setClosed(Long closed) {
        this.closed = closed;
    }

    public Long getCompletedPercentage() {
        if(getTotalScheduledEvents() > 0L)
            return Math.round((getCompleted().doubleValue() * 100)/getTotalScheduledEvents().doubleValue());
        else
            return 0L;
    }

    public Long getCompletedExcludingFailedAndClosed() {
        // TODO : what is the semantics of closed/completed/failed????  should i subtract failed?   ...ask matt.
        return completed - failed - closed;
    }

    public Double getCompletedExcludingFailedAndClosedPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                getCompletedExcludingFailedAndClosed() * 100 / getTotalScheduledEvents().doubleValue();
    }


    public Double getFailedPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                failed * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Double getClosedPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                closed * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Double getIncompletePercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                scheduled * 100 / getTotalScheduledEvents().doubleValue();
    }

}
