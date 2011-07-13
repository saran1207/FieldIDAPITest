package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.wicket.model.NetworkEntityModel;
import com.n4systems.fieldid.wicket.model.reporting.EventReportCriteriaModel;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.loader.ColumnMappingLoader;
import com.n4systems.util.persistence.search.ImmutablePostfetchingSearchDefiner;
import com.n4systems.util.persistence.search.SortDirection;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableViewAdapterDataProvider<T extends BaseEntity & NetworkEntity<T>> extends FieldIDDataProvider<T> implements ListableSortableDataProvider<T> {

    private List<T> results;
    private Class<T> clazz;
    private Integer size;
    private List<Long> currentPageIdList = new ArrayList<Long>();

    private SearchContainer searchContainer;
    private EventReportCriteriaModel reportCriteria;
    private List<String> postFetchFields = new ArrayList<String>();

    public TableViewAdapterDataProvider(Class<T> clazz, SearchContainer searchContainer, EventReportCriteriaModel reportCriteria) {
        this.clazz = clazz;
        this.searchContainer = searchContainer;
        this.reportCriteria = reportCriteria;
        setInitialSort(searchContainer, reportCriteria);
        storePostfetchFields(reportCriteria.getColumnGroups());
        //TODO: This will be different for the different types of things we might be providing data for.
        //Needs refactoring...
        postFetchFields.add("infoOptionMap");
        postFetchFields.add("asset.infoOptions");
    }

    private void storePostfetchFields(List<ColumnMappingGroupView> groups) {
        for (ColumnMappingGroupView group : groups) {
            for (ColumnMappingView column : group.getMappings()) {
                if (!"custom_created_columns".equals(column.getGroupKey()) && column.isEnabled()) {
                    postFetchFields.add(column.getPathExpression());
                }
            }
        }
    }

    private void setInitialSort(SearchContainer searchContainer, EventReportCriteriaModel reportCriteria) {
        if (reportCriteria.getSortColumn() == null) {
            setDefaultSort(searchContainer.defaultSortColumn(), searchContainer.defaultSortDirection());
        } else {
            setSort(reportCriteria.getSortColumn(), reportCriteria.getSortDirection());
        }
    }

    private void setDefaultSort(String sortColumn, SortDirection sortDirection) {
        defaultSort:
        for (ColumnMappingGroupView columnMappingGroupView : reportCriteria.getColumnGroups()) {
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
        reportCriteria.setSortColumn(mappingView);
        reportCriteria.setSortDirection(sortDirection);
        setSort(sortParam);
    }

    @Override
    public Iterator<? extends T> iterator(int first, int count) {
        return getResults(first).iterator();
    }

    @Override
    public int size() {
        if (size == null) {
            size = new SearchPerformerWithReadOnlyTransactionManagement().countPages(searchContainer, searchContainer.getSecurityFilter(), 1);
        }
        return size;
    }

    @Override
    public IModel<T> model(T object) {
        return new NetworkEntityModel<T>(clazz, object);
    }

    @Override
    public void detach() {
        super.detach();
        results = null;
        size = null;
    }
    
    private List<T> getResults(int first) {
        if (results == null) {
            int pageSize = 20;
            int page = first / pageSize;
            PageHolder<List<T>> pageHolder = runSearch(page, pageSize);
            results = pageHolder.getPageResults();
            storeIdList();
        }
        return results;
    }

    private void storeIdList() {
        currentPageIdList = new ArrayList<Long>(results.size());
        for (T result : results) {
            currentPageIdList.add(result.getId());
        }
    }

    protected PageHolder<List<T>> runSearch(int page, int pageSize) {
        SortParam sortParam = getSort();
        searchContainer.setSortColumn(null);
        searchContainer.setSortColumnId(null);
        searchContainer.setSortDirection(null);
        searchContainer.setSortJoinExpression(null);

        if (sortParam != null) {
            String sort = sortParam.getProperty();
            Long sortColumnId = Long.valueOf(sort);
            ColumnMapping columnMapping = loadMapping(sortColumnId);
            String sortExpression = columnMapping.getSortExpression();
            searchContainer.setSortColumn(sortExpression != null ? sortExpression : columnMapping.getPathExpression());
            searchContainer.setSortColumnId(sortColumnId);
            searchContainer.setSortDirection(sortParam.isAscending() ? SortDirection.ASC.getDisplayName() : SortDirection.DESC.getDisplayName());
            searchContainer.setSortJoinExpression(columnMapping.getJoinExpression());
        }

        return new SearchPerformerWithReadOnlyTransactionManagement().search(new ImmutablePostfetchingSearchDefiner<List<T>>(searchContainer, new IdentityTransformer<T>(), page, pageSize, postFetchFields), searchContainer.getSecurityFilter());
    }

    @Override
    public List<Long> getIdList() {
        return new SearchPerformerWithReadOnlyTransactionManagement().idSearch(searchContainer, searchContainer.getSecurityFilter());
    }

    private ColumnMapping loadMapping(Long id) {
        return new ColumnMappingLoader(getSecurityFilter()).id(id).load();
    }

    public List<Long> getCurrentPageIdList() {
        return currentPageIdList;
    }

}
