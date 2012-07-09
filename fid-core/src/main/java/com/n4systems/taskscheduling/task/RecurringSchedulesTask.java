package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.RecurringAssetTypeEvent;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import java.util.Date;
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

        List<RecurringAssetTypeEvent> list = persistenceManager.findAll(RecurringAssetTypeEvent.class);
        for(RecurringAssetTypeEvent event: list) {
            logger.info(event.getEventType().getName() + " " + event.getRecurrence());
            if (event.getRecurrence().requiresScheduleOn(futureDate)) {
                scheduleAnEventFor(event,futureDate);
            }
        }
    }

    private void scheduleAnEventFor(RecurringAssetTypeEvent event, LocalDate futureDate) {

        // find all assets where assetType = event.getAssetType [& tenant = tenantID.]
        List<Asset> assetsToSchedule = getAssetsByAssetType(event.getAssetType());

        //handle overrides?
        
        for (Asset asset : assetsToSchedule) {
            EventSchedule eventSchedule = new EventSchedule(asset, event.getEventType(), setTimeForEvent(futureDate.toDate(), event));
            // there should be a "SERIES-ID" stored in event....
            // save the mofo.
            //persistenceManager.save(eventSchedule);
            logger.info("AssetIdentifier: " + asset.getIdentifier());
        }
         
    }

    private Date setTimeForEvent(Date date, RecurringAssetTypeEvent event) {
        // these are UTC timezones...yes?
        // adjust date...set to 9:00am?
        return date;
    }
    
    private List<Asset> getAssetsByAssetType(AssetType assetType) {
        QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("type", assetType));
        //add owner filter, event owner = asset owner
        return persistenceManager.findAll(query);
    }
    
}
