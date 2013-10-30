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
    private List<BaseOrg> results;
    private Long size;

    public OrgsDataProvider() {
        super();
        Injector.get().inject(this);
    }

    @Override
    public Iterator iterator(int first, int count) {
        return getResults(first).iterator();
    }

    private List<BaseOrg> getResults(int first) {
        if (results == null) {
            int page = first / pageSize;
            results = orgService.search(getFilter(), page, pageSize);
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
        return orgService.getSearchCount(getFilter());
    }

    @Override
    public IModel<BaseOrg> model(BaseOrg object) {
        return new EntityModel(BaseOrg.class,object.getId());
    }

    protected String getFilter() {
        return null;
    }

    @Override
    public void detach() {
        results = null;
    }

//    protected PageHolder<TableView> runSearch(final int page) {
//        return orgService.performSearch(search, page, pageSize);
//    }

//    private void fillInStringValues(RowView row) {
//        SessionUser user = FieldIDSession.get().getSessionUser();
//        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());
//        StringRowPopulator.populateRowWithConvertedStrings(row, searchCriteria, exportContextProvider);
//    }
}
