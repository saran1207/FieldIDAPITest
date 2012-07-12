package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.RecurringAssetTypeEvent;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecurringSchedulesTask extends ScheduledTask{

    private static Logger logger = Logger.getLogger(RecurringSchedulesTask.class);

    private static final int RECURRING_EVENT_BUFFER_SIZE = 14;

    private PersistenceManager persistenceManager;

    public RecurringSchedulesTask() {
        super(60 * 30, TimeUnit.SECONDS);
        persistenceManager = ServiceLocator.getPersistenceManager();
    }

    @Override
    protected void runTask() throws Exception {
        logger.info("Starting RecurringSchedulesTask");

        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(RECURRING_EVENT_BUFFER_SIZE);

        // TODO DD : need to search for all events that don't exist yet for recently identified assets that fall under schedule?  i should do that in identify code?

        List<RecurringAssetTypeEvent> list = persistenceManager.findAll(RecurringAssetTypeEvent.class);
        for(RecurringAssetTypeEvent event: list) {
            logger.info(event.getEventType().getName() + " " + event.getRecurrence());
            for (DateTime dateTime : event.getRecurrence().getScheduledTimes(LocalDate.now(), futureDate)) {
                scheduleAnEventFor(event, dateTime);
            }
        }
    }

    private void scheduleAnEventFor(RecurringAssetTypeEvent event, DateTime futureDate) {

        List<Asset> assetsToSchedule = getAssetsByAssetType(event);

        for (Asset asset : assetsToSchedule) {
            //Check if schedule with reccurence id already exists
            EventSchedule eventSchedule = new EventSchedule(asset, event.getEventType(), futureDate.toDate());
            //TODO uncomment when new event entity is merged
            //persistenceManager.save(eventSchedule);
        }
         
    }

    private List<Asset> getAssetsByAssetType(RecurringAssetTypeEvent event) {
        QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("type", event.getAssetType()));
        if(event.getOwner() != null) {
            query.addWhere(WhereClauseFactory.create("owner", event.getOwner()));
        }
        return persistenceManager.findAll(query);
    }
    
}
