package com.n4systems.services.reporting;

import com.n4systems.model.orgs.BaseOrg;

import java.io.Serializable;
import java.util.List;

public class EventKpiRecord implements Serializable {

    // different workflow states.
	private Long completed = 0L;
	private Long incomplete = 0L;
    private Long closed = 0L;

    // different types of Complete...
	private Long failed = 0L;
    private Long passed = 0L;
    private Long na = 0L;
    private Long voyd = 0L;
	
	private BaseOrg customer;

	public Long getCompleted() {
		return completed;
	}

	public void setCompleted(List<CompletedResultRecord> completed) {
        Long count = 0L;
        for (CompletedResultRecord result:completed) {
            Long value = result.getCount();
            switch (result.getState()) {
                case PASS:
                    passed = value;
                    break;
                case FAIL:
                    failed = value;
                    break;
                case NA:
                    na = value;
                    break;
                case VOID:
                    voyd = value;
                    break;
            }
            count+= value;
        }
		this.completed = count;
	}

	public Long getIncomplete() {
		return incomplete;
	}

	public void setIncomplete(Long incomplete) {
		this.incomplete = incomplete;
	}

	public long getFailed() {
		return failed;
	}

	public BaseOrg getCustomer() {
		return customer;
	}

	public void setCustomer(BaseOrg customer) {
		this.customer = customer;
	}

	public Long getTotalScheduledEvents() {
		return completed + incomplete + closed;
	}

    public Long getClosed() {
        return closed;
    }

    public void setClosed(Long closed) {
        this.closed = closed;
    }

    public Long getNa() {
        return na;
    }

    public Long getPassed() {
        return passed;
    }

    public Long getVoyd() {
        return voyd;
    }

}
