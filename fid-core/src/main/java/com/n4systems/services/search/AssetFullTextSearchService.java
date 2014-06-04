package com.n4systems.services.search;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.services.search.field.IndexField;
import com.n4systems.services.search.writer.AssetIndexWriter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class AssetFullTextSearchService extends FullTextSearchService {

    @Autowired private AssetIndexWriter assetIndexWriter;

    @Override
    protected String getIndexPath(Tenant tenant) {
        return assetIndexWriter.getIndexPath(tenant);
    }

    @Override
    protected IndexField[] getIndexFields() {
        return AssetIndexField.values();
    }

    @Override
    protected void addSecondaryOrgSecurity(BaseOrg owner, BooleanQuery securityQuery) {
        Long id = owner.getId();  // secondary can see primary, and this secondary

        BooleanQuery secondaryIsNullQuery = new BooleanQuery();

        secondaryIsNullQuery.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
        secondaryIsNullQuery.add(NumericRangeQuery.newLongRange(AssetIndexField.SECONDARY_ID.getField(), 0L, null, true, true), BooleanClause.Occur.MUST_NOT);

        securityQuery.add(new BooleanClause(NumericRangeQuery.newLongRange(AssetIndexField.SECONDARY_ID.getField(), id, id, true, true), BooleanClause.Occur.SHOULD));
        securityQuery.add(secondaryIsNullQuery, BooleanClause.Occur.SHOULD);
    }

    @Override
    protected void addCustomerOrDivisionOrgSecurity(BaseOrg owner, BooleanQuery securityQuery) {
        addOrgFilter(securityQuery, owner.getSecondaryOrg(), AssetIndexField.SECONDARY_ID.getField());
        addOrgFilter(securityQuery, owner.getCustomerOrg(), AssetIndexField.CUSTOMER_ID.getField());
        addOrgFilter(securityQuery, owner.getDivisionOrg(), AssetIndexField.DIVISION_ID.getField());
    }

}
