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
import com.n4systems.util.excel.ExcelXSSFBuilder;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;
import com.n4systems.util.views.TableViewExcelHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public abstract class ExcelExportService<T extends SearchCriteria> extends DownloadService<T> {
	private static final String SHEET_NAME = "Report";

    public ExcelExportService() {
        super("excelReportDownload", ContentType.EXCEL);
    }

    @Override
    public void generateFile(T criteria, OutputStream oStream, boolean useSelection, int resultLimit, int pageSize) throws ReportException {
        generateFile(criteria, oStream, useSelection, resultLimit, pageSize, false);
    }


    //This is done the same as the old way, but uses OutputStreams and goat blood to run.  Yes, you will have to
    //sacrifice a goat to the Internet Gods if this breaks... it's the only way to fix it!!
    public void generateFile(T criteria, OutputStream oStream, boolean useSelection, int resultLimit, int pageSize, boolean exceptionOnEmptyReport) throws ReportException {
		int totalResults = calcTotalResults(useSelection, criteria, resultLimit);
		if (exceptionOnEmptyReport && totalResults == 0) {
			throw new EmptyReportException();
		}

		User user = getCurrentUser();

        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());
		TableViewExcelHandler tableHandler = new TableViewExcelHandler(createCellHandlers(criteria, exportContextProvider));

        ResultTransformer<TableView> resultTransformer = new ResultTransformerFactory().createResultTransformer(criteria);

        ExcelXSSFBuilder excelBuilder = new ExcelXSSFBuilder(new DateTimeDefiner(user));
		excelBuilder.createSheet(SHEET_NAME);
		excelBuilder.setSheetTitles(SHEET_NAME, getColumnTitles(criteria));

		int totalPages = (int)Math.ceil(totalResults / (double) pageSize);
		for (int page = 0; page < totalPages; page++) {
			excelBuilder.addSheetData(SHEET_NAME, getSearchPage(criteria, resultTransformer, page, pageSize, useSelection, tableHandler));
		}

        try {
            excelBuilder.writeToStream(oStream);
        } catch (IOException e) {
            throw new ReportException(e);
        }
    }

	private int calcTotalResults(boolean useSelection, T criteria, int resultLimit) {
		int totalResults;
		if (useSelection) {
			totalResults = criteria.getSelection().getNumSelectedIds();
		} else {
			totalResults = countTotalResults(criteria, resultLimit);
		}
		return totalResults;
	}

	private TableView getSearchPage(T criteria, ResultTransformer<TableView> resultTransformer, int pageNumber, int pageSize, boolean useSelection, TableViewExcelHandler tableHandler) {
		TableView page = performSearch(criteria, resultTransformer, pageNumber, pageSize, useSelection).getPageResults();
		tableHandler.handle(page);
		return page;
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
        return columns.stream().map(ColumnMappingView::getLocalizedLabel).collect(Collectors.toList());
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
