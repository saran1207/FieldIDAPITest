package com.n4systems.fieldid.service.event;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.CriteriaTrendsEntry;
import com.n4systems.model.EventType;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.services.reporting.CriteriaTrendsResultCountByCriteriaRecord;
import com.n4systems.services.reporting.CriteriaTrendsResultCountRecord;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CriteriaTrendsService extends FieldIdPersistenceService {

    @Autowired
    private DateService dateService;

    public List<CriteriaTrendsResultCountRecord> findCriteriaTrends(EventType eventType, DateRange dateRange) {
        QueryBuilder<CriteriaTrendsResultCountRecord> query = new QueryBuilder<CriteriaTrendsResultCountRecord>(CriteriaTrendsEntry.class, new TenantOnlySecurityFilter(getCurrentTenant()));

        NewObjectSelect select = new NewObjectSelect(CriteriaTrendsResultCountRecord.class);
        List<String> args = Lists.newArrayList("COUNT(*)", "resultText");
        select.setConstructorArgs(args);
        query.setSelectArgument(select);

        DateRangeTerm drt = new DateRangeTerm("completedDate", dateService.calculateFromDate(dateRange), dateService.calculateToDate(dateRange));

        for (WhereClause<?> whereClause : drt.getWhereParameters()) {
            query.addWhere(whereClause);
        }

        query.addSimpleWhere("eventType", eventType);

        query.addGroupBy("resultText");

        return persistenceService.findAll(query);
    }

    public List<CriteriaTrendsResultCountByCriteriaRecord> findCriteriaTrendsByCriteria(EventType eventType, DateRange dateRange, String resultText) {
        QueryBuilder<CriteriaTrendsResultCountByCriteriaRecord> query = new QueryBuilder<CriteriaTrendsResultCountByCriteriaRecord>(CriteriaTrendsEntry.class, new TenantOnlySecurityFilter(getCurrentTenant()));

        NewObjectSelect select = new NewObjectSelect(CriteriaTrendsResultCountByCriteriaRecord.class);
        List<String> args = Lists.newArrayList("COUNT(*)", "resultText", "criteriaName");
        select.setConstructorArgs(args);
        query.setSelectArgument(select);

        DateRangeTerm drt = new DateRangeTerm("completedDate", dateService.calculateFromDate(dateRange), dateService.calculateToDate(dateRange));

        for (WhereClause<?> whereClause : drt.getWhereParameters()) {
            query.addWhere(whereClause);
        }

        query.addSimpleWhere("eventType", eventType);
        query.addSimpleWhere("resultText", resultText);

        query.addGroupBy("criteriaName");

        return persistenceService.findAll(query);
    }


}
