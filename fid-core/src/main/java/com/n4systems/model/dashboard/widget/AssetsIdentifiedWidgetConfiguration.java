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

import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithGranularity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_assets_identified")
@PrimaryKeyJoinColumn(name="id")
public class AssetsIdentifiedWidgetConfiguration extends WidgetConfiguration implements ConfigurationWithGranularity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_id")
	private BaseOrg org;

	@Enumerated(EnumType.STRING)
	@Column(name="date_range", nullable=false)	
	private ChartDateRange dateRange = ChartDateRange.THIS_QUARTER;

    @Enumerated(EnumType.STRING)
    @Column(name="granularity", nullable=false)
    private ChartGranularity granularity;
	
	@AllowSafetyNetworkAccess
	public BaseOrg getOrg() {
		return org;
	}
	
	public void setOrg(BaseOrg org) { 
		this.org = org;
	}

	public void setDateRange(ChartDateRange range) {
		this.dateRange = range;
	}

	public ChartDateRange getDateRange() {
		return dateRange;
	}

	@Override
	public AssetsIdentifiedWidgetConfiguration copy() {
		AssetsIdentifiedWidgetConfiguration copy = (AssetsIdentifiedWidgetConfiguration) super.copy();
		copy.setOrg(getOrg());
		copy.setDateRange(getDateRange());
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
}
