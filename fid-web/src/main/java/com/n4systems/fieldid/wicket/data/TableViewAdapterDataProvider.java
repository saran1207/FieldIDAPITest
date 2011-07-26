package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.viewhelpers.handlers.CellHandlerFactory;
import com.n4systems.fieldid.viewhelpers.handlers.WebOutputHandler;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.RowModel;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.loader.ColumnMappingLoader;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.TableViewTransformer;
import com.n4systems.util.views.RowView;
import com.n4systems.util.views.TableView;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableViewAdapterDataProvider<T extends BaseEntity & NetworkEntity<T>> extends FieldIDDataProvider<RowView> implements ListableSortableDataProvider<RowView>, WebContextProvider {

    @SpringBean
    private ReportService reportService;

    private List<RowView> results;
    private Class<T> clazz;
    private Integer size;
    private List<Long> currentPageIdList = new ArrayList<Long>();

    private SearchContainer searchContainer;
    private EventReportCriteriaModel reportCriteria;

    public TableViewAdapterDataProvider(Class<T> clazz, SearchContainer searchContainer, EventReportCriteriaModel reportCriteria) {
        this.clazz = clazz;
        this.searchContainer = searchContainer;
        this.reportCriteria = reportCriteria;
        setInitialSort(searchContainer, reportCriteria);
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
    public Iterator<? extends RowView> iterator(int first, int count) {
        return getResults(first).iterator();
    }

    @Override
    public int size() {
        if (size == null) {
            size = reportService.countPages(reportCriteria, 1L);
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
        for (ColumnMappingView column : reportCriteria.getSortedStaticAndDynamicColumns()) {
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

    protected PageHolder<TableView> runSearch(int page, int pageSize) {
        return reportService.eventReport(reportCriteria, createResultTransformer(), page, pageSize);
    }

    private ResultTransformer<TableView> createResultTransformer() {
		List<String> columns = new ArrayList<String>();
		for(ColumnMappingView mapping: reportCriteria.getSortedStaticAndDynamicColumns()) {
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

    @Override
    public List<Long> getIdList() {
        return reportService.idSearch(reportCriteria);
    }

    private ColumnMapping loadMapping(Long id) {
        return new ColumnMappingLoader(getSecurityFilter()).id(id).load();
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
