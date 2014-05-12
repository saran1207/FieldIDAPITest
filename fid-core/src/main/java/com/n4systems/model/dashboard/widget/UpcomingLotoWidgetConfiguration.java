package com.n4systems.model.dashboard.widget;

import com.n4systems.model.dashboard.widget.interfaces.ConfigurationWithPeriod;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.ChartPeriod;
import com.n4systems.util.chart.RangeType;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_upcoming_loto")
@PrimaryKeyJoinColumn(name="id")
public class UpcomingLotoWidgetConfiguration extends WidgetConfiguration implements ConfigurationWithPeriod {

	@Enumerated(EnumType.STRING)
	@Column(name="date_range", nullable=false)	
	private RangeType rangeType = RangeType.FOREVER;

    @Column(name="period", nullable=false)
    private Integer period = 30;

	public void setRangeType(RangeType rangeType) {
		this.rangeType = rangeType;
	}

	public RangeType getRangeType() {
		return rangeType;
	}

	@Override
	public UpcomingLotoWidgetConfiguration copy() {
		UpcomingLotoWidgetConfiguration copy = (UpcomingLotoWidgetConfiguration) super.copy();
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
