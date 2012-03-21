package com.n4systems.fieldid.service.event;

import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.download.CellHandlerFactory;
import com.n4systems.fieldid.service.download.DownloadService;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.TableGenerationContextImpl;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.ExcelBuilder;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;
import com.n4systems.util.views.TableViewExcelHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventExcelExportService extends DownloadService<EventReportCriteria> {

    @Autowired
    private ReportService reportService;

    private static final int PAGE_SIZE = 256;

    public EventExcelExportService() {
        super("excelReportDownload", ContentType.EXCEL);
    }

    @Override
    protected void generateFile(EventReportCriteria criteria, File file, String linkName) throws ReportException {
        User user = getCurrentUser();

        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());

        DateTimeDefiner dateTimeDefiner = new DateTimeDefiner(user);

        int selectedItems = criteria.getSelection().getNumSelectedIds();
        int totalPages = (int)(selectedItems / (long)PAGE_SIZE);

        final List<ColumnMappingView> sortedStaticAndDynamicColumns = criteria.getSortedStaticAndDynamicColumns(true);

        // we'll initalize the table view with one 1 row .. this will resize dynamically as we append tables
		TableView masterTable = new TableView(0, sortedStaticAndDynamicColumns.size());

        final ResultTransformer<TableView> resultTransformer = new ResultTransformerFactory().createResultTransformer(criteria);

        int pageNumber = 0;
		do {


			masterTable.append(reportService.performSearch(criteria, resultTransformer, pageNumber, PAGE_SIZE, true).getPageResults());
			pageNumber++;

		} while(pageNumber <= totalPages);

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

    private List<String> getColumnTitles(EventReportCriteria criteria) {
        final List<ColumnMappingView> columns = criteria.getSortedStaticAndDynamicColumns(true);
        List<String> columnTitles = new ArrayList<String>();
        for (ColumnMappingView columnMappingView : columns) {
            columnTitles.add(columnMappingView.getLocalizedLabel());
        }
        return columnTitles;
    }

    private ExcelOutputHandler[] createCellHandlers(EventReportCriteria criteria, TableGenerationContext exportContextProvider) {
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
