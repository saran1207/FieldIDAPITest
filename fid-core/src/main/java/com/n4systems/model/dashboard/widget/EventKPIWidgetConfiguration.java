package com.n4systems.model.dashboard.widget;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.FloatingDateRange;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_event_kpi")
@PrimaryKeyJoinColumn(name="id")
public class EventKPIWidgetConfiguration extends WidgetConfiguration {

    @OneToMany
    @JoinTable(name = "widget_configurations_event_kpi_orgs",
                    joinColumns = @JoinColumn(name = "config_id"),
                    inverseJoinColumns = @JoinColumn(name = "org_id"))
    @IndexColumn(name="orderIdx")
    private List<BaseOrg> orgs = new ArrayList<BaseOrg>();

	@Enumerated(EnumType.STRING)
	@Column(name="date_range", nullable=false)	    
	private FloatingDateRange dateRange = FloatingDateRange.THIS_WEEK;
    
    public List<BaseOrg> getOrgs() {
        return orgs;
    }
    
    public void setOrgs(List<BaseOrg> orgs) {
        this.orgs = orgs;
    }

    @Override
    public EventKPIWidgetConfiguration copy() {
        EventKPIWidgetConfiguration copy = (EventKPIWidgetConfiguration) super.copy();
        copy.setOrgs(new ArrayList<BaseOrg>(orgs));
		copy.setDateRange(getDateRange());        
        return copy;
    }

	public void setDateRange(FloatingDateRange dateRange) {
		this.dateRange = dateRange;
	}

	public FloatingDateRange getDateRange() {
		return dateRange;
	}

}
