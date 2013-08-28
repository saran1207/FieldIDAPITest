package com.n4systems.services.search;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

public class CriteriaTrendsIndexerServiceForOther extends CriteriaTrendsIndexerService {

    @Override
    protected QueryBuilder<CriteriaTrendsIndexQueueItem> createQueryBuilder() {
        QueryBuilder<CriteriaTrendsIndexQueueItem> query = new QueryBuilder<CriteriaTrendsIndexQueueItem>(CriteriaTrendsIndexQueueItem.class, new OpenSecurityFilter());
        query.addWhere(WhereParameter.Comparator.NE, "type", "type", CriteriaTrendsIndexQueueItem.CriteriaTrendsIndexQueueItemType.TENANT);
        return query;
    }

}
