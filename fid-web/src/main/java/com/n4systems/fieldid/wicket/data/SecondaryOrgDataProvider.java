package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class SecondaryOrgDataProvider extends FieldIDDataProvider<SecondaryOrg> {

    private OrgListFilterCriteria criteria;
    private @SpringBean OrgService orgService;
    private int pageSize = 20;
    private List<? extends SecondaryOrg> results;
    private Long size;

    public SecondaryOrgDataProvider() {
        super();
        Injector.get().inject(this);
    }

    public SecondaryOrgDataProvider(OrgListFilterCriteria criteria) {
        this.criteria = criteria;
        setSort(criteria.getOrder(), criteria.isAscending() ? SortOrder.ASCENDING: SortOrder.DESCENDING);
    }

    @Override
    public Iterator iterator(int first, int count) {
        return getResults(first).iterator();
    }

    private List<? extends SecondaryOrg> getResults(int first) {
        if (results == null) {
            int page = first / pageSize;
            criteria.withOrder(getSort().getProperty(), getSort().isAscending());
            results = orgService.getSecondaryOrgs(criteria, page, pageSize);
        }
        return results;
    }

    public SecondaryOrgDataProvider withPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public int size() {
        if (this.size==null) {
            size = getResultSize();
        }
        return size.intValue();
    }

    /*private Long getResultSize() {return orgService.getSearchSecondaryOrgCount(getTextFilter(), getTypeFilter());}*/
    private Long getResultSize() {
        return Long.valueOf(orgService.countSecondaryOrgs(criteria));
    }

    @Override
    public IModel<SecondaryOrg> model(SecondaryOrg object) {
        return new EntityModel(SecondaryOrg.class,object.getId());
    }

    protected String getTextFilter() {
        return null;
    }

    protected Class<? extends SecondaryOrg> getTypeFilter() {
        return null;
    }

    @Override
    public void detach() {
        results = null;
        size = null;
    }

}
