package com.n4systems.services.search;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.search.field.EventIndexField;
import com.n4systems.services.search.field.IndexField;
import com.n4systems.services.search.writer.EventIndexWriter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.springframework.beans.factory.annotation.Autowired;

public class EventFullTextSearchService extends FullTextSearchService {

    @Autowired private EventIndexWriter eventIndexWriter;

    @Override
    protected IndexField[] getIndexFields() {
        return EventIndexField.values();
    }

    @Override
    protected String getIndexPath(Tenant tenant) {
        return eventIndexWriter.getIndexPath(tenant);
    }

    @Override
    protected void addSecondaryOrgSecurity(BaseOrg owner, BooleanQuery securityQuery) {
        Long id = owner.getId();  // secondary can see primary, and this secondary
        securityQuery.add(new BooleanClause(NumericRangeQuery.newLongRange("_secondaryOrgId", id, id, true, true), BooleanClause.Occur.SHOULD));
        securityQuery.add(NumericRangeQuery.newLongRange(EventIndexField.SECONDARY_ID.getField(), 0L, null, true, true), BooleanClause.Occur.MUST_NOT);  // tricky way of saying "should be null"
    }

    @Override
    protected void addCustomerOrDivisionOrgSecurity(BaseOrg owner, BooleanQuery securityQuery) {
        addOrgFilter(securityQuery, owner.getSecondaryOrg(), EventIndexField.SECONDARY_ID.getField());
        addOrgFilter(securityQuery, owner.getCustomerOrg(), EventIndexField.CUSTOMER_ID.getField());
        addOrgFilter(securityQuery, owner.getDivisionOrg(), EventIndexField.DIVISION_ID.getField());
    }

}
