package com.n4systems.model.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;
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
		// see WEB-2542 for details on how to handle failed, then passed events on same asset. 
		QueryBuilder<Event> builder = createQueryBuilder(filter);
		
		WhereParameterGroup statusGroup = new WhereParameterGroup("statusgroup");		
		statusGroup.addClause(WhereClauseFactory.create(Comparator.EQ, "failStatus", "status", Status.FAIL, null, ChainOp.OR));
		statusGroup.addClause(WhereClauseFactory.create(Comparator.EQ, "passStatus", "status", Status.PASS, null, ChainOp.OR));		
		builder.addWhere(statusGroup);		
		
		builder.addWhere(Comparator.GE, "date", "date", getFromDate());
		builder.addOrder("date");

		if(setting != null) {
			applyNotificationFilters(builder);
		}
		
		builder.getPostFetchPaths().addAll(Arrays.asList(Event.ALL_FIELD_PATHS));
		List<Event> results = builder.getResultList(em);
		
		results = filterEarlierEventsForSameAsset(results);		

		Collections.sort(results, new java.util.Comparator<Event>() {
			@Override public int compare(Event a, Event b) {
				return (int) (a.getDate().getTime()-b.getDate().getTime());
			} 
		});		
		return results;
	}

	/*pkg protected so i can extract override in test*/
	QueryBuilder<Event> createQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<Event>(Event.class, filter);
	}

	private List<Event> filterEarlierEventsForSameAsset(List<Event> results) {
		// precondition : list is sorted ascending by date.
		final Map<String, Event> events = new HashMap<String, Event>();		
		for (Event event:results) {			
			String key = makeEventAssetKey(event);     
			if (Status.FAIL.equals(event.getStatus())) {
				events.put(key, event);
			} else if (Status.PASS.equals(event.getStatus())) {
				events.remove(key);
			}
		}
		return new ArrayList<Event>(events.values());
	}

	private String makeEventAssetKey(Event event) {
		return event.getType().getId()+":"+event.getAsset().getId();
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
