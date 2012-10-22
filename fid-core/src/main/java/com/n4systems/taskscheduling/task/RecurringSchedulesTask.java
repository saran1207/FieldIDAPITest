package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.RecurringAssetTypeEvent;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.EventScheduleService;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecurringSchedulesTask extends ScheduledTask{

    private static Logger logger = Logger.getLogger(RecurringSchedulesTask.class);

    public static final int RECURRING_EVENT_BUFFER_SIZE = 14;

    public RecurringSchedulesTask() {
        super(60 * 30, TimeUnit.SECONDS);
    }

    public EventScheduleService getEventScheduleService() {
        return ServiceLocator.getEventScheduleService();
    }

    public PersistenceManager getPersistenceManager() {
        return ServiceLocator.getPersistenceManager();
    }

    @Override
    protected void runTask() throws Exception {
        logger.info("Starting RecurringSchedulesTask");

        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(RECURRING_EVENT_BUFFER_SIZE);

        List<RecurringAssetTypeEvent> list = getRecurringAssetTypeEvents();
        for(RecurringAssetTypeEvent event: list) {
            for (LocalDateTime dateTime : event.getRecurrence().getScheduledTimes(LocalDate.now(), futureDate)) {
                scheduleAnEventFor(event, dateTime);
            }
        }
    }

    private List<RecurringAssetTypeEvent> getRecurringAssetTypeEvents() {
        QueryBuilder<RecurringAssetTypeEvent> query = new QueryBuilder<RecurringAssetTypeEvent>(RecurringAssetTypeEvent.class, new OpenSecurityFilter());
        return getPersistenceManager().findAll(query);

    }

    private void scheduleAnEventFor(RecurringAssetTypeEvent event, LocalDateTime futureDate) {

        List<Asset> assetsToSchedule = getAssetsByAssetType(event);

        for (Asset asset : assetsToSchedule) {
            if(!checkIfScheduleExists(asset, event, futureDate)) {
                Event schedule = new Event();
                schedule.setAsset(asset);
                schedule.setType(event.getEventType());
                schedule.setDueDate(futureDate.toDate());
                schedule.setTenant(asset.getTenant());
                schedule.setRecurringEvent(event);
                schedule.setOwner(asset.getOwner());
                getEventScheduleService().createSchedule(schedule);
            }
        }
         
    }

    private boolean checkIfScheduleExists(Asset asset, RecurringAssetTypeEvent event, LocalDateTime futureDate) {
        QueryBuilder<Event> query = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("asset", asset));
        query.addWhere(WhereClauseFactory.create("recurringEvent", event));

        //A simple equals does not work due to comparison problems comparing Timestamps and Date see java.sql.Timestamp
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "dueDate", futureDate.minusMillis(1).toDate()));
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "to", "dueDate", futureDate.toDate()));

        return getPersistenceManager().findCount(query) > 0;
    }


    private List<Asset> getAssetsByAssetType(RecurringAssetTypeEvent event) {
        QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("type", event.getAssetType()));
        if(event.getOwner() != null) {
            query.addWhere(WhereClauseFactory.create("owner", event.getOwner()));
        }
        return getPersistenceManager().findAll(query);
    }
    
}
