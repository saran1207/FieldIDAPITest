package com.n4systems.services.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.ConfigService;
import com.n4systems.services.search.writer.CriteriaTrendsIndexWriter;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
public abstract class CriteriaTrendsIndexerService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(CriteriaTrendsIndexerService.class);

    @Autowired private CriteriaTrendsIndexWriter criteriaTrendsIndexWriter;
    @Autowired private PersistenceService persistenceService;
    @Autowired private ConfigService configService;

    @Scheduled(fixedDelay = 2000)
    public void processIndexQueue() {
        Boolean trendsIndexEnabled = configService.getBoolean(ConfigEntry.TRENDS_INDEX_ENABLED);
        if (!trendsIndexEnabled) {
            logger.info(getClass().getSimpleName()+": Disabled");
            return;
        }
        long startTime = System.currentTimeMillis();
        logger.info(getClass().getSimpleName() + ": Running");

        QueryBuilder<CriteriaTrendsIndexQueueItem> query = createQueryBuilder();
        query.setLimit(configService.getInteger(ConfigEntry.TRENDS_INDEX_SIZE));

        List<CriteriaTrendsIndexQueueItem> items = persistenceService.findAll(query);
        logger.info(getClass().getSimpleName() + " queue length = " + items.size());
        for (CriteriaTrendsIndexQueueItem item : items) {
            try {
                processIndexQueueItem(item);
                persistenceService.deleteAny(item);
            } catch (Exception e) {
                logger.warn(getClass().getSimpleName()+": Failed for " + item.getType() + ":" + item.getId(), e);
            }
        }
        logger.info(getClass().getSimpleName()+": Completed " + (System.currentTimeMillis() - startTime) + "ms");
    }

    protected abstract QueryBuilder<CriteriaTrendsIndexQueueItem> createQueryBuilder();

    public void processIndexQueueItem(CriteriaTrendsIndexQueueItem item) {
        switch(item.getType()) {
            case EVENT_INSERT:
            case EVENT_UPDATE:
                updateIndex(persistenceService.findNonSecure(Event.class, item.getItemId()));
                break;
            case TENANT:
                updateTenant(persistenceService.findNonSecure(Tenant.class, item.getItemId()));
                break;
        }
    }

    private void updateIndex(Event event) {
        criteriaTrendsIndexWriter.index(event.getTenant(), Arrays.asList(event), /*ignored*/false);
    }

    private void updateTenant(Tenant tenant) {
        criteriaTrendsIndexWriter.reindexItems(tenant, new QueryBuilder<Event>(Event.class, new TenantOnlySecurityFilter(tenant)).addSimpleWhere("workflowState", WorkflowState.COMPLETED));
    }

    public void placeItemInQueueForTenant(Long tenantId) {
        if(!queueItemExists(tenantId, CriteriaTrendsIndexQueueItem.CriteriaTrendsIndexQueueItemType.TENANT)) {
            CriteriaTrendsIndexQueueItem item = new CriteriaTrendsIndexQueueItem();
            item.setType(CriteriaTrendsIndexQueueItem.CriteriaTrendsIndexQueueItemType.TENANT);
            item.setItemId(tenantId);

            persistenceService.saveAny(item);
        }
    }

    private boolean queueItemExists(Long id, CriteriaTrendsIndexQueueItem.CriteriaTrendsIndexQueueItemType type) {
        QueryBuilder<CriteriaTrendsIndexQueueItem> builder = new QueryBuilder<CriteriaTrendsIndexQueueItem>(CriteriaTrendsIndexQueueItem.class, new OpenSecurityFilter());
        builder.addSimpleWhere("itemId", id);
        builder.addSimpleWhere("type", type);

        return persistenceService.exists(builder);
    }

}
