package com.n4systems.fieldid.wicket.pages.admin.reports;

import com.n4systems.fieldid.service.admin.AdminReportsService;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.excel.ExcelXSSFBuilder;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class AdminReportsPage extends FieldIDAdminPage {

    static public Logger logger = LoggerFactory.getLogger(AdminReportsPage.class);

    static private long MAX_DAYS_IN_REPORT = 32;
    @SpringBean
    private AdminReportsService adminReportsService;

    private FeedbackPanel feedbackPanel;

    public AdminReportsPage() {
        super();
        addComponents();
    }

   /* @Override
    protected void onBeforeRender() {
        System.out.println("AdminReportsPage.onBeforeRender");
        //Session.get().cleanupFeedbackMessages();
        super.onBeforeRender();
    }*/

    private void addComponents() {
        addFeedbackPanel();
        addUsageReport();
    }

    private void addFeedbackPanel() {
        feedbackPanel = new FeedbackPanel("feedbackPanel");
        feedbackPanel.add(new AttributeAppender("style", new Model("text-align: center; color:red; padding: 0px 10px"), " "));
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
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
                //Session.get().getFeedbackMessages().clear();
                Session.get().cleanupFeedbackMessages();
                //target.add(feedbackPanel);
                //Session.get().dirty();
                //feedbackPanel.getFeedbackMessagesModel().getObject().clear();
                final Date fromDate = fromDateModel.getObject();
                final Date toDate = toDateModel.getObject();

                long diffInMs = toDate.getTime() - fromDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);

                System.out.println("Diff in ms " + diffInMs + ", diff in days " + diffInDays);

                if (diffInMs <= 0) {
                    error(getString("startDateMustBeBeforeEndDateMsg"));
                }
                else
                if (diffInDays > MAX_DAYS_IN_REPORT) {
                    error(getFormattedString("timePeriodExceedsMaxMsg",
                            new String[] {new Long(diffInDays).toString(), new Long(MAX_DAYS_IN_REPORT).toString()}));
                }
                else {
                    System.out.println("Processing report");
                    AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {

                        @Override
                        public void write(Response output) {
                            generateUsageReport(output.getOutputStream(), fromDate, toDate);
                        }
                    };

                    String downloadFileName = "UsageReport." + ContentType.EXCEL.getExtension();
                    ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, downloadFileName);
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                }
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
                    adminReportsService.getUsageReportData(fromDate, toDate));
            excelBuilder.writeToStream(outputStream);
        }
        catch(Exception ex) {
            logger.error("Generate Usage Report failed", ex);
        }
    }

    private String getFormattedString(String key, Object[] parameters) {
        String label = getString(key);
        MessageFormat format = new MessageFormat(label);
        format.applyPattern(label);

        return format.format(parameters);
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
