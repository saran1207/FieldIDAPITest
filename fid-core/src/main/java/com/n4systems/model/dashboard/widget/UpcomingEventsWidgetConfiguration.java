package com.n4systems.model.dashboard.widget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithPeriod;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.chart.ChartPeriod;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_upcoming_events")
@PrimaryKeyJoinColumn(name="id")
public class UpcomingEventsWidgetConfiguration extends WidgetConfiguration implements ConfigurationWithPeriod {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_id")
	private BaseOrg org;

	@Enumerated(EnumType.STRING)
	@Column(name="date_range", nullable=false)	
	private RangeType rangeType = RangeType.FOREVER;

    @Column(name="period", nullable=false)
    private Integer period = 30;
	
	@AllowSafetyNetworkAccess
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
	public UpcomingEventsWidgetConfiguration copy() {
		UpcomingEventsWidgetConfiguration copy = (UpcomingEventsWidgetConfiguration) super.copy();
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
