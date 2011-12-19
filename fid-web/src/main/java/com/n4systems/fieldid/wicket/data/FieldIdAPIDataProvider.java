package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.fieldid.viewhelpers.handlers.CellHandlerFactory;
import com.n4systems.fieldid.viewhelpers.handlers.WebOutputHandler;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.RowModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteriaModel;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.TableViewTransformer;
import com.n4systems.util.views.RowView;
import com.n4systems.util.views.TableView;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FieldIdAPIDataProvider extends FieldIDDataProvider<RowView> implements ListableSortableDataProvider<RowView>, WebContextProvider {

    private List<RowView> results;
    private Integer size;
    private List<Long> currentPageIdList = new ArrayList<Long>();

    private SearchCriteriaModel searchCriteria;

    private String defaultSortColumn;
    private SortDirection defaultSortDirection;

    public FieldIdAPIDataProvider(SearchCriteriaModel searchCriteria, String defaultSortColumn, SortDirection defaultSortDirection) {
        this.searchCriteria = searchCriteria;
        this.defaultSortColumn = defaultSortColumn;
        this.defaultSortDirection = defaultSortDirection;
        setInitialSort(searchCriteria);
    }

    protected abstract int getResultCount();
    protected abstract PageHolder<TableView> runSearch(int page, int pageSize);

    private void setInitialSort(SearchCriteriaModel searchCriteria) {
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
        CellHandlerFactory cellHandlerFactory = new CellHandlerFactory(this);
        List<String> rowValues = new ArrayList<String>();
        int index = 0;
        for (ColumnMappingView column : searchCriteria.getSortedStaticAndDynamicColumns()) {
            WebOutputHandler handler = cellHandlerFactory.getHandler(column.getOutputHandler());
            String cellValue = handler.handleWeb(row.getId(), row.getValues().get(index));
            rowValues.add(cellValue);
            index++;
        }
        row.setStringValues(rowValues);
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
		List<String> columns = new ArrayList<String>();
		for(ColumnMappingView mapping: searchCriteria.getSortedStaticAndDynamicColumns()) {
			if (mapping != null) {
				columns.add(mapping.getPathExpression());
			}
		}

		ResultTransformer<TableView> transformer = null;
		try {
			transformer = new TableViewTransformer("id", columns);
		} catch (ParseException e) {
            throw new RuntimeException(e);
		}

		return transformer;
    }

    public List<Long> getCurrentPageIdList() {
        return currentPageIdList;
    }

    @Override
    public String getText(String key) {
        return new FIDLabelModel(key).getObject();
    }

    @Override
    public SessionUser getSessionUser() {
        return FieldIDSession.get().getSessionUser();
    }

}
