package com.n4systems.fieldid.wicket.pages.admin.reports;

import com.n4systems.fieldid.service.admin.AdminReportsService;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.excel.ExcelXSSFBuilder;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.TimeZone;


public class AdminReportsPage extends FieldIDAdminPage {

    static public Logger logger = LoggerFactory.getLogger(AdminReportsPage.class);

    @SpringBean
    private AdminReportsService adminReportsService;

    public AdminReportsPage() {
        super();
        addComponents();
    }

    private void addComponents() {
        addUsageReport();
    }

    private void addUsageReport() {
        final IModel<Date> fromDateModel = Model.of(new Date());
        final IModel<Date> toDateModel = Model.of(new Date());
        DateTimePicker fromDatePicker = new AdminConsoleDateTimePicker("usageReportFromDate", fromDateModel).withNoAllDayCheckbox();
        DateTimePicker toDatePicker = new AdminConsoleDateTimePicker("usageReportToDate", toDateModel).withNoAllDayCheckbox();
        add(fromDatePicker);
        add(toDatePicker);

        Link downloadUsageReportLink = new Link("createUsageReport") {
            @Override
            public void onClick() {
                System.out.println("Clicked download link");
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {

                    @Override
                    public void write(Response output) {
                        generateUsageReport(output.getOutputStream(), fromDateModel.getObject(), toDateModel.getObject());
                    }
                };

                String downloadFileName = "UsageReport." + ContentType.EXCEL.getExtension();
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, downloadFileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };

        add(downloadUsageReportLink);
    }

    private void generateUsageReport(OutputStream outputStream, Date fromDate, Date toDate) {

        try {
            String SHEET_NAME = "USAGE_REPORT";
            ExcelXSSFBuilder excelBuilder = new ExcelXSSFBuilder(new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault()));
            excelBuilder.createSheet(SHEET_NAME,
                    adminReportsService.getUsageReportColumnHeader(),
                    adminReportsService.getUageReportData(fromDate, toDate));
            excelBuilder.writeToStream(outputStream);
        }
        catch(Exception ex) {
            logger.error("Generate Usage Report failed", ex);
        }
    }

    private class AdminConsoleDateTimePicker extends DateTimePicker {

        public AdminConsoleDateTimePicker (String id, IModel<? extends Date> dateModel) {
            super(id, dateModel);
        }

        @Override
        protected String getDateFormat() {
            return "MM/dd/yy";
        }
        @Override
        protected String getJQueryDateFormat() {
            return "mm/dd/y";
        }
    }

}
