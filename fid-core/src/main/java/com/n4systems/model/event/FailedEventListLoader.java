package com.n4systems.model.event;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.Status;
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

public class FailedEventListLoader extends ListLoader<Event>{
	
	private Clock clock;
	private SimpleFrequency frequency;
	private NotificationSetting setting;
	
	public FailedEventListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Event> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, filter);
		
		builder.addWhere(WhereClauseFactory.create("status", Status.FAIL));
		builder.addWhere(Comparator.GE, "date", "date", getFromDate());

		if(setting != null) {
			applyNotificationFilters(builder);
		}
		
		builder.addOrder("date");
		builder.getPostFetchPaths().addAll(Arrays.asList(Event.ALL_FIELD_PATHS));
		return builder.getResultList(em);
	}

	private void applyNotificationFilters(QueryBuilder<Event> builder) {
		if(setting.getOwner() != null) {
			builder.applyFilter(new OwnerAndDownFilter(setting.getOwner()));
		}
		if(!setting.getAssetTypes().isEmpty()) {
			builder.addSimpleWhere("asset.type.id", setting.getAssetTypes().get(0));
		}

		if (setting.getAssetTypes().isEmpty() && setting.getAssetTypeGroup() != null) {
			builder.addSimpleWhere("asset.type.group.id", setting.getAssetTypeGroup());
		}
		
		if(setting.getEventTypes().isEmpty() && setting.getEventTypeGroup() !=null){
			builder.addSimpleWhere("type.group.id", setting.getEventTypeGroup());
		}

		if (setting.getAssetStatus() != null) {
			builder.addSimpleWhere("asset.assetStatus.id", setting.getAssetStatus());
		}		
		
		if(!setting.getEventTypes().isEmpty()) {
			builder.addSimpleWhere("type.id", setting.getEventTypes().get(0));
		}
	}

	private Date getFromDate() {
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
