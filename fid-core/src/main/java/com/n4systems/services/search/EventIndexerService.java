package com.n4systems.services.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.ConfigService;
import com.n4systems.services.search.writer.EventIndexWriter;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Transactional
public class EventIndexerService extends FieldIdPersistenceService {

    private static Logger logger = Logger.getLogger(EventIndexerService.class);

    private @Autowired EventIndexWriter eventIndexWriter;

    private @Autowired ConfigService configService;
    private @Resource PlatformTransactionManager transactionManager;

    @Scheduled(fixedDelay = 2000)
    public void processIndexQueue() {
        if (!configService.getBoolean(ConfigEntry.EVENT_INDEX_ENABLED)) {
            logger.info(getClass().getSimpleName() +": Disabled");
            return;
        }
        long startTime = System.currentTimeMillis();
        logger.info(getClass().getSimpleName() +": Running");

        QueryBuilder<EventIndexQueueItem> query = new QueryBuilder<EventIndexQueueItem>(EventIndexQueueItem.class);
        query.setLimit(configService.getInteger(ConfigEntry.EVENT_INDEX_SIZE));

        List<EventIndexQueueItem> items = persistenceService.findAll(query);
        logger.info(getClass().getSimpleName() + " queue length = " + items.size());
        for (EventIndexQueueItem item : items) {
            try {
                processIndexQueueItem(item);
                persistenceService.deleteAny(item);
            } catch (Exception e) {
                logger.warn(getClass().getSimpleName() +": Failed for " + item.getType() + ":" + item.getId(), e);
            }
        }
        logger.info(getClass().getSimpleName() +": Completed " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void processIndexQueueItem(EventIndexQueueItem item) {
        switch(item.getType()) {
            case EVENT_INSERT:
                updateIndex(persistenceService.findNonSecure(Event.class, item.getItemId()), false);
                break;
            case EVENT_UPDATE:
                updateIndex(persistenceService.findNonSecure(Event.class, item.getItemId()), true);
                break;
            case TENANT:
                updateTenant(persistenceService.findNonSecure(Tenant.class, item.getItemId()));
                break;
        }
    }

    private void updateIndex(Event event, boolean update) {
        if (event != null && event.getWorkflowState() == WorkflowState.COMPLETED) {
            eventIndexWriter.index(event.getTenant(), Arrays.asList(event), update);
        }
    }

    private void updateTenant(Tenant tenant) {
        eventIndexWriter.reindexItems(tenant, new QueryBuilder<Event>(Event.class, new TenantOnlySecurityFilter(tenant)).addSimpleWhere("workflowState", WorkflowState.COMPLETED));
    }

    public void placeItemInQueueForTenant(Long tenantId) {
        if(!queueItemExists(tenantId, EventIndexQueueItem.EventIndexQueueItemType.TENANT)) {
            EventIndexQueueItem item = new EventIndexQueueItem();
            item.setType(EventIndexQueueItem.EventIndexQueueItemType.TENANT);
            item.setItemId(tenantId);

            persistenceService.saveAny(item);
        }
    }

    private boolean queueItemExists(Long id, EventIndexQueueItem.EventIndexQueueItemType type) {
        QueryBuilder<EventIndexQueueItem> builder = new QueryBuilder<EventIndexQueueItem>(EventIndexQueueItem.class, new OpenSecurityFilter());
        builder.addSimpleWhere("itemId", id);
        builder.addSimpleWhere("type", type);

        return persistenceService.exists(builder);
    }

}
