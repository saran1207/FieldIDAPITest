package com.n4systems.fieldid.wicket.pages.admin.reports;

import com.n4systems.fieldid.service.admin.AdminReportsService;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.excel.ExcelXSSFBuilder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class AdminReportsPage extends FieldIDAdminPage {

    static public Logger logger = LoggerFactory.getLogger(AdminReportsPage.class);

    static private long MAX_DAYS_IN_REPORT = 32;
    @SpringBean
    private AdminReportsService adminReportsService;

    private FeedbackPanel feedbackPanel;

    final IModel<Date> fromDateModel = Model.of(new Date());
    final IModel<Date> toDateModel = Model.of(new Date());

    public AdminReportsPage() {
        super();
        addComponents();
    }

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

        final AJAXDownload download = new AJAXDownload() {
            @Override
            protected IResourceStream getResourceStream() {
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {

                    @Override
                    public void write(Response output) {
                        generateUsageReport(output.getOutputStream(), fromDateModel.getObject(), toDateModel.getObject());
                    }
                };

                String downloadFileName = "UsageReport." + ContentType.EXCEL.getExtension();
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, downloadFileName);
                return handler.getResourceStream();
            }
        };
        add(download);

        DateTimePicker fromDatePicker = new AdminConsoleDateTimePicker("usageReportFromDate", fromDateModel).withNoAllDayCheckbox();
        DateTimePicker toDatePicker = new AdminConsoleDateTimePicker("usageReportToDate", toDateModel).withNoAllDayCheckbox();
        add(fromDatePicker);
        add(toDatePicker);

        AjaxLink downloadUsageReportLink = new AjaxLink("createUsageReport") {
            /* Use Ajax for the download link so we can clear out any old feedback messages.
               The normal methods for clearing out the messages don't work with a regular
               link. */
            @Override
            public void onClick(AjaxRequestTarget target) {

                target.add(feedbackPanel);

                /* The start date needs to be adjusted to be at the beginning of the day and the end date needs
                   to be adjusted to be at the end of the day. The default time portion returned by the calendar
                   widget is the current time. */

                final Date fromDate = Date.from(
                            ZonedDateTime.ofInstant(fromDateModel.getObject().toInstant(),
                                    ZoneId.systemDefault()).toLocalDate().atStartOfDay(
                                            ZoneId.systemDefault()).toInstant().atZone(ZoneId.systemDefault()).toInstant());

                final Date toDate = Date.from(
                        ZonedDateTime.ofInstant(toDateModel.getObject().toInstant(),
                                ZoneId.systemDefault()).toLocalDate().atStartOfDay(
                                ZoneId.systemDefault()).plusDays(1).toInstant().atZone(ZoneId.systemDefault()).toInstant());

                long diffInMs = toDate.getTime() - fromDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);

                if (diffInMs <= 0) {
                    error(getString("startDateMustBeBeforeEndDateMsg"));
                }
                else
                if (diffInDays > MAX_DAYS_IN_REPORT) {
                    error(getFormattedString("timePeriodExceedsMaxMsg",
                            new String[] {new Long(diffInDays).toString(), new Long(MAX_DAYS_IN_REPORT).toString()}));
                }
                else {
                    download.initiate(target);
                }
            }
        };

        add(downloadUsageReportLink);
    }

    private void generateUsageReport(OutputStream outputStream, Date fromDate, Date toDate) {

        try {
            String SHEET_NAME = "USAGE_REPORT";
            ExcelXSSFBuilder excelBuilder = new ExcelXSSFBuilder(new DateTimeDefiner("mm/dd/yyyy", TimeZone.getDefault()));
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

    /**
     * This class copied from https://cwiki.apache.org/confluence/display/WICKET/AJAX+update+and+file+download+in+one+blow
     */
    abstract public class AJAXDownload extends AbstractAjaxBehavior
    {
        private boolean addAntiCache;

        public AJAXDownload() {
            this(true);
        }

        public AJAXDownload(boolean addAntiCache) {
            super();
            this.addAntiCache = addAntiCache;
        }

        /**
         * Call this method to initiate the download.
         */
        public void initiate(AjaxRequestTarget target) {

            String url = getCallbackUrl().toString();
            if (addAntiCache) {
                url = url + (url.contains("?") ? "&" : "?");
                url = url + "antiCache=" + System.currentTimeMillis();
            }
            // the timeout is needed to let Wicket release the channel
            target.appendJavaScript("setTimeout(\"window.location.href='" + url + "'\", 100);");
        }

        public void onRequest() {
            ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(getResourceStream(),getFileName());
            handler.setContentDisposition(ContentDisposition.ATTACHMENT);
            getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
        }

        /**
         * Override this method for a file name which will let the browser prompt with a save/open dialog.

         */
        protected String getFileName() {
            return "UsageReport." + ContentType.EXCEL.getExtension();
        }

        /**
         * Hook method providing the actual resource stream.
         */
        abstract protected IResourceStream getResourceStream();

    }

}
