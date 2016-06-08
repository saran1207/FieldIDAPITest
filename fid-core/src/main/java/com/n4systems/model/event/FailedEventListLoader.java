package com.n4systems.model.event;

import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.time.Clock;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FailedEventListLoader extends ListLoader<ThingEvent>{
	
	private Clock clock;
	private SimpleFrequency frequency;
	protected NotificationSetting setting;
	
	public FailedEventListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<ThingEvent> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ThingEvent> builder = new QueryBuilder<ThingEvent>(ThingEvent.class, filter);
		
		builder.addWhere(WhereClauseFactory.create("eventResult", EventResult.FAIL));
		builder.addWhere(Comparator.GE, "date", "completedDate", getFromDate());  // this needs to be converted toUTC(timeZone)!!!

		if(setting != null) {
			applyNotificationFilters(builder);
		}
		
		builder.addOrder("completedDate");
		builder.getPostFetchPaths().addAll(Arrays.asList(Event.ALL_FIELD_PATHS));
		return builder.getResultList(em);
	}

	protected void applyNotificationFilters(QueryBuilder<ThingEvent> builder) {
		if(setting.getOwner() != null) {
			builder.applyFilter(new OwnerAndDownFilter(setting.getOwner()));
		}
		if(setting.getAssetType() != null) {
			builder.addSimpleWhere("asset.type", setting.getAssetType());
		}

		if (setting.getAssetType() == null && setting.getAssetTypeGroup() != null) {
			builder.addSimpleWhere("asset.type.group", setting.getAssetTypeGroup());
		}
		
		if(setting.getEventType() == null && setting.getEventTypeGroup() !=null){
			builder.addSimpleWhere("type.group", setting.getEventTypeGroup());
		}

		if (setting.getAssetStatus() != null) {
			builder.addSimpleWhere("asset.assetStatus", setting.getAssetStatus());
		}		
		
		if(setting.getEventType() != null) {
			builder.addSimpleWhere("type.id", setting.getEventType());
		}
	}

	public Date getFromDate() {
		Date date = new PlainDate(clock.currentTime());
		if (frequency.equals(SimpleFrequency.DAILY)) {
			date = DateHelper.increment(date, DateHelper.DAY, -1);
		}else if (frequency.getGroupLabel().equals(SimpleFrequency.WEEKLY_SUNDAY.getGroupLabel())) {
			date = DateHelper.increment(date, DateHelper.WEEK, -1);
		}else if (frequency.getGroupLabel().equals(SimpleFrequency.MONTHLY_FIRST.getGroupLabel())) {
			date = DateHelper.increment(date, DateHelper.MONTH, -1);
		}
		return date;
	}

	public FailedEventListLoader setClock(Clock clock) {
		this.clock = clock;
		return this;
	}

	public FailedEventListLoader setFrequency(SimpleFrequency frequency) {
		this.frequency = frequency;
		return this;
	}

	public FailedEventListLoader setNotificationSetting(NotificationSetting setting) {
		this.setting = setting;
		return this;
	}
}
