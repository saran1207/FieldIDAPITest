package com.n4systems.services.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.search.writer.CriteriaTrendsIndexWriter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
public class CriteriaTrendsIndexerService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(CriteriaTrendsIndexerService.class);

    @Autowired private CriteriaTrendsIndexWriter criteriaTrendsIndexWriter;
    @Autowired private PersistenceService persistenceService;

    @Scheduled(fixedDelay = 5000)
    public void processIndexQueue() {
        long startTime = System.currentTimeMillis();
        logger.info("ProcessIndexQueue: Running");

        List<CriteriaTrendsIndexQueueItem> items = persistenceService.findAllNonSecure(CriteriaTrendsIndexQueueItem.class);
        logger.info(getClass().getSimpleName() + " queue length = " + items.size());
        for (CriteriaTrendsIndexQueueItem item : items) {
            try {
                processIndexQueueItem(item);
                persistenceService.deleteAny(item);
            } catch (Exception e) {
                logger.warn("ProcessIndexQueue: Failed for " + item.getType() + ":" + item.getId(), e);
            }
        }
        logger.info("ProcessIndexQueue: Completed " + (System.currentTimeMillis() - startTime) + "ms");
    }

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
