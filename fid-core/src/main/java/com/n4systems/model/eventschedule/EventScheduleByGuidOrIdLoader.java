package com.n4systems.model.eventschedule;

import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;

public class EventScheduleByGuidOrIdLoader extends SecurityFilteredLoader<Event> {
    private static final Logger logger= Logger.getLogger(EventScheduleByGuidOrIdLoader.class);

	private String mobileGuid;
	private long id;
	

	public EventScheduleByGuidOrIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Event load(EntityManager em, SecurityFilter filter) {
		Event schedule = null;
		
		if (mobileGuid != null && mobileGuid.trim().length() > 0) {
			schedule = loadByGuid(em, filter);
		}

        if (schedule == null && id > 0) {
            logger.warn("Finding by legacy schedule mobile GUID "  + mobileGuid + " and/or schedule id : " + id);
            schedule = loadByLegacyScheduleId(id, filter, em);
        }

        if (schedule == null) {
            // CAVEAT : at this point we can't be sure whether it wasn't found because the scheduled event was created on mobile
            // and hasn't been uploaded yet or something wrong with guid.   we'll log it for now even though this could be noisy.
            // remove this after a couple of iterations when we can be sure schedule id is no longer used.
            logger.warn("Failed to find schedule with mobile GUID " + mobileGuid + " and/or schedule id : " + id);
        }

        return schedule;
	}

    private Event loadByLegacyScheduleId(long id, SecurityFilter filter, EntityManager em) {
        QueryBuilder<Event> queryBuilder = getQueryBuilder(filter);
        queryBuilder.addSimpleWhere("scheduleId", id);
        return queryBuilder.getSingleResult(em);
    }

    protected Event loadByGuid(EntityManager em,
			SecurityFilter filter) {
		QueryBuilder<Event> query = getQueryBuilder(filter);
		query.addSimpleWhere("mobileGUID", mobileGuid);
		
		return query.getSingleResult(em);
	}
	

	public EventScheduleByGuidOrIdLoader setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
		return this;
	}
	
	public EventScheduleByGuidOrIdLoader setId(long id) {
		this.id = id;
		return this;
	}
	
	protected QueryBuilder<Event> getQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<Event>(Event.class, filter);
	}

}
