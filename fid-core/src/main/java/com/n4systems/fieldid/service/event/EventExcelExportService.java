package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.PageHolder;
import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.download.CellHandlerFactory;
import com.n4systems.fieldid.service.download.DownloadService;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.TableGenerationContextImpl;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.fieldid.service.export.ExcelExportService;
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

public class EventExcelExportService extends ExcelExportService<EventReportCriteria> {

    @Autowired
    private ReportService reportService;

    @Override
    protected int countTotalResults(EventReportCriteria criteria) {
        return reportService.countPages(criteria, 1L);
    }

    @Override
    protected PageHolder<TableView> performSearch(EventReportCriteria criteria, ResultTransformer<TableView> resultTransformer, int pageNumber, int pageSize, boolean useSelection) {
        return reportService.performSearch(criteria,resultTransformer,pageNumber, pageSize, useSelection);
    }
}
