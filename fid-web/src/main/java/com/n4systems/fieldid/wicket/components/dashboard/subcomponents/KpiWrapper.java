package com.n4systems.fieldid.wicket.components.dashboard.subcomponents;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.EventKpiRecord;

import java.io.Serializable;

public class KpiWrapper implements Serializable {

    private EventKpiRecord kpi;

    public KpiWrapper(EventKpiRecord kpi) {
        this.kpi = kpi;
    }

    public Long getTotalScheduledEvents() {
        return kpi.getTotalScheduledEvents();
    }

    public Long getPassed() {
        return kpi.getPassed();
    }

    public Long getVoyd() {
        return kpi.getVoyd();
    }

    public Long getIncomplete() {
        return kpi.getIncomplete();
    }

    public long getFailed() {
        return kpi.getFailed();
    }

    public Long getClosed() {
        return kpi.getClosed();
    }

    public Long getCompleted() {
        return kpi.getCompleted();
    }

    public BaseOrg getCustomer() {
        return kpi.getCustomer();
    }

    public Long getNa() {
        return kpi.getNa();
    }

    public Long getCompletedPercentage() {
        if(getTotalScheduledEvents() > 0L)
            return Math.round((getCompleted().doubleValue() * 100)/getTotalScheduledEvents().doubleValue());
        else
            return 0L;
    }

    public Double getFailedPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                getFailed() * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Double getPassedPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                getPassed() * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Double getNaPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                getNa() * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Double getClosedPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                getClosed() * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Double getIncompletePercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                getIncomplete() * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Long getCompletedAndClosed() {
        return getCompleted()+getClosed();
    }

    public Double getCompletedAndClosedPercentage() {
        return getTotalScheduledEvents() == 0 ? 0 :
                getCompletedAndClosed() * 100 / getTotalScheduledEvents().doubleValue();
    }

    public Double getFailedRelativePercentage() {
        return getCompleted()==0L ? 0.0 :
                getFailed() * 100 / getCompletedAndClosed();
    }

    public Double getNaRelativePercentage() {
        return getCompleted()==0L ? 0.0 :
                getNa() * 100 / getCompletedAndClosed();
    }

    public Double getPassedRelativePercentage() {
        return getCompleted()==0L ? 0.0 :
                getPassed() * 100 / getCompletedAndClosed();
    }

    public Double getClosedRelativePercentage() {
        return getCompleted()==0L ? 0.0 :
                getClosed() * 100 / getCompletedAndClosed();
    }

}
