package com.n4systems.services.search;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class CriteriaTrendsIndexerServiceForTenants extends CriteriaTrendsIndexerService {

    @Override
    protected QueryBuilder<CriteriaTrendsIndexQueueItem> createQueryBuilder() {
        QueryBuilder<CriteriaTrendsIndexQueueItem> query = new QueryBuilder<CriteriaTrendsIndexQueueItem>(CriteriaTrendsIndexQueueItem.class, new OpenSecurityFilter());
        query.addSimpleWhere("type", CriteriaTrendsIndexQueueItem.CriteriaTrendsIndexQueueItemType.TENANT);
        return query;
    }

}
