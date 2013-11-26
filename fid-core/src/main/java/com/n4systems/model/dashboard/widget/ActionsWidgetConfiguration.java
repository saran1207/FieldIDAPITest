package com.n4systems.model.dashboard.widget;

import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;

import javax.persistence.*;

@Entity
@Table(name = "widget_configurations_actions")
@PrimaryKeyJoinColumn(name="id")
public class ActionsWidgetConfiguration extends WidgetConfiguration  {

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_id")
	private BaseOrg org;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable=true)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="action_type", nullable=true)
    private ThingEventType actionType;

	@Enumerated(EnumType.STRING)
	@Column(name="date_range", nullable=false)
	private RangeType rangeType = RangeType.FOREVER;

    public ThingEventType getActionType() {
        return actionType;
    }

    public void setActionType(ThingEventType actionType) {
        this.actionType = actionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
	public ActionsWidgetConfiguration copy() {
		ActionsWidgetConfiguration copy = (ActionsWidgetConfiguration) super.copy();
		copy.setOrg(getOrg());
		copy.setRangeType(getRangeType());
        copy.setUser(getUser());
        copy.setActionType(getActionType());
		return copy;
	}

    public DateRange getDateRange() {
        return new DateRange(getRangeType());
    }
}
