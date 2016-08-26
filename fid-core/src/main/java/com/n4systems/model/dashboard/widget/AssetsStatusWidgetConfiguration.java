package com.n4systems.model.dashboard.widget;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_assets_status")
@PrimaryKeyJoinColumn(name="id")
public class AssetsStatusWidgetConfiguration extends WidgetConfiguration {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_id")
	private BaseOrg org;

	@Enumerated(EnumType.STRING)
	@Column(name="date_range", nullable=false)	
	private RangeType rangeType = RangeType.FOREVER;
	
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
	public AssetsStatusWidgetConfiguration copy() {
		AssetsStatusWidgetConfiguration copy = (AssetsStatusWidgetConfiguration) super.copy();
		copy.setOrg(getOrg());
		copy.setRangeType(getRangeType());
		return copy;
	}

    public DateRange getDateRange() {
        return new DateRange(getRangeType());
    }
	
}
