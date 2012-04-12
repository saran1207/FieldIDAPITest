package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.download.*;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.RowModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.RowView;
import com.n4systems.util.views.TableView;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FieldIdAPIDataProvider extends FieldIDDataProvider<RowView> implements ListableSortableDataProvider<RowView> {

    private List<RowView> results;
    private Integer size;
    private List<Long> currentPageIdList = new ArrayList<Long>();

    private SearchCriteria searchCriteria;

    private String defaultSortColumn;
    private SortDirection defaultSortDirection;

    public FieldIdAPIDataProvider(SearchCriteria searchCriteria, String defaultSortColumn, SortDirection defaultSortDirection) {
        this.searchCriteria = searchCriteria;
        this.defaultSortColumn = defaultSortColumn;
        this.defaultSortDirection = defaultSortDirection;
        setInitialSort(searchCriteria);
    }

    protected abstract int getResultCount();
    protected abstract PageHolder<TableView> runSearch(int page, int pageSize);

    private void setInitialSort(SearchCriteria searchCriteria) {
        if (searchCriteria.getSortColumn() == null) {
            setDefaultSort(defaultSortColumn, defaultSortDirection);
        } else {
            setSort(searchCriteria.getSortColumn(), searchCriteria.getSortDirection());
        }
    }

    private void setDefaultSort(String sortColumn, SortDirection sortDirection) {
        defaultSort:
        for (ColumnMappingGroupView columnMappingGroupView : searchCriteria.getColumnGroups()) {
            for (ColumnMappingView mappingView : columnMappingGroupView.getMappings()) {
                if (mappingView.getSortExpression() != null && mappingView.getSortExpression().equals(sortColumn)) {
                    setSort(mappingView, sortDirection);
                    break defaultSort;
                }
            }
        }
    }

    private void setSort(ColumnMappingView mappingView, SortDirection sortDirection) {
        SortParam sortParam = new SortParam(mappingView.getDbColumnId().toString(), sortDirection == SortDirection.ASC);
        searchCriteria.setSortColumn(mappingView);
        searchCriteria.setSortDirection(sortDirection);
        setSort(sortParam);
    }

    @Override
    public Iterator<? extends RowView> iterator(int first, int count) {
        return getResults(first).iterator();
    }

    @Override
    public int size() {
        if (size == null) {
            size = getResultCount();
        }
        return size;
    }

    @Override
    public IModel<RowView> model(RowView row) {
        fillInStringValues(row);
        return new RowModel(row);
    }

    private void fillInStringValues(RowView row) {
        SessionUser user = FieldIDSession.get().getSessionUser();
        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());
        StringRowPopulator.populateRowWithConvertedStrings(row,searchCriteria, exportContextProvider);
    }

    @Override
    public void detach() {
        super.detach();
        results = null;
        size = null;
    }
    
    private List<RowView> getResults(int first) {
        if (results == null) {
            int pageSize = 20;
            int page = first / pageSize;
            PageHolder<TableView> pageHolder = runSearch(page, pageSize);
            results = pageHolder.getPageResults().getRows();
            storeIdList();
        }
        return results;
    }

    private void storeIdList() {
        currentPageIdList = new ArrayList<Long>(results.size());
        for (RowView result : results) {
            currentPageIdList.add(result.getId());
        }
    }

    protected ResultTransformer<TableView> createResultTransformer() {
        return new ResultTransformerFactory().createResultTransformer(searchCriteria);
    }

    public List<Long> getCurrentPageIdList() {
        return currentPageIdList;
    }

}
