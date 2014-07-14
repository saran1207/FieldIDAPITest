package com.n4systems.model.dashboard.widget;

import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithPeriod;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.ChartPeriod;
import com.n4systems.util.chart.RangeType;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_upcoming_procedure_audits")
@PrimaryKeyJoinColumn(name="id")
public class UpcomingProcedureAuditsWidgetConfiguration extends WidgetConfiguration implements ConfigurationWithPeriod {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    private BaseOrg org;

    @Enumerated(EnumType.STRING)
    @Column(name="date_range", nullable=false)
    private RangeType rangeType = RangeType.FOREVER;

    @Column(name="period", nullable=false)
    private Integer period = 30;


    public BaseOrg getOrg() {
        return org;
    }

    public void setOrg(BaseOrg org) {
        this.org = org;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public RangeType getRangeType() {
        return rangeType;
    }

    @Override
    public UpcomingProcedureAuditsWidgetConfiguration copy() {
        UpcomingProcedureAuditsWidgetConfiguration copy = (UpcomingProcedureAuditsWidgetConfiguration) super.copy();
        copy.setOrg(getOrg());
        copy.setRangeType(getRangeType());
        return copy;
    }

    @Override
    public Integer getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(Integer period) {
        this.period = period;
    }

    public ChartPeriod getChartPeriod() {
        return ChartPeriod.valueOf(period);
    }

    public DateRange getDateRange() {
        return new DateRange(getRangeType());
    }
}
