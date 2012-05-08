package com.n4systems.model.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.Status;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;

public class SmartFailedEventListLoader extends FailedEventListLoader {
		
	public SmartFailedEventListLoader(SecurityFilter filter) {
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
		
		builder.addWhere(Comparator.GE, "date", "schedule.completedDate", getFromDate());
		builder.addOrder("schedule.completedDate");

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

}
