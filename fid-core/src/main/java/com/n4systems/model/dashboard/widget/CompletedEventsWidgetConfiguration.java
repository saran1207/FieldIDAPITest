package com.n4systems.model.dashboard.widget;

import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithGranularity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.RangeType;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_completed_events")
@PrimaryKeyJoinColumn(name="id")
public class CompletedEventsWidgetConfiguration extends WidgetConfiguration implements ConfigurationWithGranularity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_id")
	private BaseOrg org;

	@Enumerated(EnumType.STRING)
	@Column(name="date_range", nullable=false)	
	private RangeType rangeType = RangeType.THIS_QUARTER;

    @Enumerated(EnumType.STRING)
    @Column(name="granularity", nullable=false)
    private ChartGranularity granularity = ChartGranularity.WEEK;
	
	@AllowSafetyNetworkAccess
	public BaseOrg getOrg() {
		return org;
	}
	
	public void setOrg(BaseOrg org) { 
		this.org = org;
	}

	public void setDateRangeType(RangeType rangeType) {
		this.rangeType = rangeType;
	}

	public RangeType getDateRangeType() {
		return rangeType;
	}

	@Override
	public CompletedEventsWidgetConfiguration copy() {
		CompletedEventsWidgetConfiguration copy = (CompletedEventsWidgetConfiguration) super.copy();
		copy.setOrg(getOrg());
		copy.setDateRangeType(getDateRangeType());
		return copy;
	}

    @Override
    public ChartGranularity getGranularity() {
        return granularity;
    }

    @Override
    public void setGranularity(ChartGranularity granularity) {
        this.granularity = granularity;
    }

    public DateRange getDateRange() {
        return new DateRange(rangeType);
    }
}
