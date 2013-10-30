package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class OrgsDataProvider implements IDataProvider<BaseOrg> {

    private @SpringBean OrgService orgService;
    private int pageSize = 20;
    private List<? extends BaseOrg> results;
    private Long size;

    public OrgsDataProvider() {
        super();
        Injector.get().inject(this);
    }

    @Override
    public Iterator iterator(int first, int count) {
        return getResults(first).iterator();
    }

    private List<? extends BaseOrg> getResults(int first) {
        if (results == null) {
            int page = first / pageSize;
            results = orgService.search(getTextFilter(), getTypeFilter(), page, pageSize);
        }
        return results;
    }

    public OrgsDataProvider withPageSize(int pageSize) {
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

    private Long getResultSize() {
        return orgService.getSearchCount(getTextFilter(), getTypeFilter());
    }

    @Override
    public IModel<BaseOrg> model(BaseOrg object) {
        return new EntityModel(BaseOrg.class,object.getId());
    }

    protected String getTextFilter() {
        return null;
    }

    protected Class<? extends BaseOrg> getTypeFilter() {
        return null;
    }

    @Override
    public void detach() {
        results = null;
        size = null;
    }

}

