package com.n4systems.fieldid.service.export;

import com.n4systems.ejb.PageHolder;
import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.download.CellHandlerFactory;
import com.n4systems.fieldid.service.download.DownloadService;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.TableGenerationContextImpl;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.ExcelBuilder;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;
import com.n4systems.util.views.TableViewExcelHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ExcelExportService<T extends SearchCriteria> extends DownloadService<T> {

    public ExcelExportService() {
        super("excelReportDownload", ContentType.EXCEL);
    }

    @Override
    @Transactional
    public void generateFile(T criteria, File file, boolean useSelection, int resultLimit, int pageSize) throws ReportException {
        generateFile(criteria, file, useSelection, resultLimit, pageSize, false);
    }

    @Transactional
    public void generateFile(T criteria, File file, boolean useSelection, int resultLimit, int pageSize, boolean exceptionOnEmptyReport) throws ReportException {
        User user = getCurrentUser();

        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());

        DateTimeDefiner dateTimeDefiner = new DateTimeDefiner(user);

        int totalResults;

        if (useSelection) {
            totalResults = criteria.getSelection().getNumSelectedIds();
        } else {
            totalResults = countTotalResults(criteria, resultLimit);
        }

        if (exceptionOnEmptyReport && totalResults == 0) {
            throw new EmptyReportException();
        }

        int totalPages = (int)Math.ceil(totalResults / (double)pageSize);

        final List<ColumnMappingView> sortedStaticAndDynamicColumns = criteria.getSortedStaticAndDynamicColumns(true);

        // we'll initalize the table view with one 1 row .. this will resize dynamically as we append tables
        TableView masterTable = new TableView(0, sortedStaticAndDynamicColumns.size());

        final ResultTransformer<TableView> resultTransformer = new ResultTransformerFactory().createResultTransformer(criteria);

        int pageNumber = 0;
        do {
            masterTable.append(performSearch(criteria, resultTransformer, pageNumber, pageSize, useSelection).getPageResults());
            pageNumber++;

        } while(pageNumber < totalPages);

        // we now need to run all the cell handlers on the table so the values are properly converted

        TableViewExcelHandler tableHandler = new TableViewExcelHandler(createCellHandlers(criteria, exportContextProvider));
        tableHandler.handle(masterTable);

        // create an excel builder and add our data
        ExcelBuilder excelBuilder = new ExcelBuilder(dateTimeDefiner);
        excelBuilder.createSheet("Report", getColumnTitles(criteria), masterTable);

        //write the file
        try {
            excelBuilder.writeToFile(file);
        } catch (IOException e) {
            throw new ReportException(e);
        }
    }

    private int countTotalResults(T criteria, int resultLimit) {
        int totalResults = countTotalResults(criteria);
        if (resultLimit == 0) {
            return totalResults;
        }
        return Math.min(resultLimit, totalResults);
    }

    protected abstract int countTotalResults(T criteria);

    protected abstract PageHolder<TableView> performSearch(T criteria, ResultTransformer<TableView> resultTransformer, int pageNumber, int pageSize, boolean useSelection);

    private List<String> getColumnTitles(SearchCriteria criteria) {
        final List<ColumnMappingView> columns = criteria.getSortedStaticAndDynamicColumns(true);
        List<String> columnTitles = new ArrayList<String>();
        for (ColumnMappingView columnMappingView : columns) {
            columnTitles.add(columnMappingView.getLocalizedLabel());
        }
        return columnTitles;
    }

    private ExcelOutputHandler[] createCellHandlers(SearchCriteria criteria, TableGenerationContext exportContextProvider) {
        final List<ColumnMappingView> selectedColumns = criteria.getSortedStaticAndDynamicColumns();
        ExcelOutputHandler[] handlers = new ExcelOutputHandler[selectedColumns.size()];
        CellHandlerFactory factory = new CellHandlerFactory(exportContextProvider);

        int i = 0;
        for (ColumnMappingView selectedColumn : selectedColumns) {
            handlers[i++] = factory.getHandler(selectedColumn.getOutputHandler());
        }
        return handlers;
    }


}
