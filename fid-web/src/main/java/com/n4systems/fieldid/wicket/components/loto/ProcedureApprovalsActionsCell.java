package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.LotoPrintoutReportMapProducer;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.UrlEncoder;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TimeZone;

/*
Actions
If the user logged in is the creator of the procedure they will have:
    View - https://n4.fieldid.com/fieldid/w/procedureDefinitionPrint?4&id=281
    Edit - https://n4.fieldid.com/fieldid/w/procedureDef?3&id=281
    Delete
If the user logged in is part of the approval group (if one is set) they will have:
    Start Approval Process - https://n4.fieldid.com/fieldid/w/procedureDef?3&id=281
    View
    Delete
If the user logged in is not the author or the approver they will only have:
    View
 */

public class ProcedureApprovalsActionsCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private LotoReportService lotoReportService;

    @SpringBean
    private SvgGenerationService svgGenerationService;


    private static final Logger logger = Logger.getLogger(ProcedureApprovalsActionsCell.class);


    public ProcedureApprovalsActionsCell(String id, IModel<ProcedureDefinition> procedureDefinitionModel, final ProcedureListPanel procedureListPanel) {
        super(id);

        final AjaxLink<Void> deleteLink;
        final BookmarkablePageLink<Void> startApprovalLink;
        final BookmarkablePageLink<Void> editLink;
        final BookmarkablePageLink<Void> viewLink;

        final ProcedureDefinition procedureDefinition = (ProcedureDefinition) procedureDefinitionModel.getObject();

        editLink = new BookmarkablePageLink<Void>("editLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinitionModel.getObject().getId())) {
            public boolean isVisible() {
                return ( isAuthor(procedureDefinition) || ( isApprover(procedureDefinition) && isRejected(procedureDefinition) ) );
            }
        };

        startApprovalLink = new BookmarkablePageLink<Void>("startApprovalLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinitionModel.getObject().getId())) {
            public boolean isVisible() {
                return (isApprover(procedureDefinition) && !isRejected(procedureDefinition));
            }
        };

        deleteLink =  new AjaxLink<Void>("deleteLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {

                try {
                    procedureDefinitionService.deleteProcedureDefinition(procedureDefinition);
                } catch (Exception e) {
                    error(new FIDLabelModel("error.eventdeleting").getObject());
                    target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());
                }

                info(new FIDLabelModel("message.procedure_definitions.delete").getObject());
                target.add(procedureListPanel);
                target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());

            }

            public boolean isVisible() {
                return ((isAuthor(procedureDefinition) || isApprover(procedureDefinition)) );
            }

        };


        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList") {
            public boolean isVisible() {
                return ( editLink.isVisible() || startApprovalLink.isVisible() ||  deleteLink.isVisible());
            }

        };
        actionsList.setOutputMarkupId(true);

        actionsList.add(editLink);
        actionsList.add(startApprovalLink);
        actionsList.add(deleteLink);

        add(actionsList);

        //Add the print buttons
        WebMarkupContainer optionsContainer2 = new WebMarkupContainer("optionsContainer2");

        Link shortLink = new Link("shortLink") {
            @Override
            public void onClick() {
                //Set up short form stuff
                HashMap<String, Object> reportMap = Maps.newHashMap();

                try {
                    reportMap.putAll(lotoReportService.getShortJasperMap());
                } catch (IOException e) {
                    logger.error("There was a problem downloading the .jasper files on a Short Form print for user " +
                            FieldIDSession.get().getSessionUser().getId(), e);
                }

                reportMap.putAll(new LotoPrintoutReportMapProducer(procedureDefinition.getProcedureCode() + " Printout",
                        procedureDefinition,
                        new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault()),
                        s3Service,
                        svgGenerationService).produceMap());

                if(reportMap.get("isolationPointSubreport") == null) {
                    logger.warn("Short Form Report for ProcedureDefinition with ID " + procedureDefinition.getId() +
                            " is missing the Isolation Points Subreport... did you expect this?");
                }

                if(reportMap.get("imageSubreport") == null) {
                    logger.warn("Short Form Report for ProcedureDefinition with ID " + procedureDefinition.getId() +
                            " is missing the Images Subreport... did you expect this?");
                }

                File tempReport = doPrint((InputStream)reportMap.get("main"), reportMap);

                String downloadName = procedureDefinition.getProcedureCode() + "_-_Short";

                downloadName = downloadName.replaceAll(" ", "_");

                handleDownload(tempReport, downloadName);
            }
        };

        shortLink.add(new Label("label", new FIDLabelModel("label.short_form")));
        optionsContainer2.add(shortLink);

        Link longLink = new Link("longLink") {
            @Override
            public void onClick() {
                HashMap<String, Object> reportMap = Maps.newHashMap();

                try {
                    reportMap.putAll(lotoReportService.getLongJasperMap());
                } catch (IOException e) {
                    logger.error("There was a problem downloading the .jasper files on a Long Form print for user " +
                            FieldIDSession.get().getSessionUser().getId(), e);
                }

                reportMap.putAll(new LotoPrintoutReportMapProducer(procedureDefinition.getProcedureCode() + " Printout",
                        procedureDefinition,
                        new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault()),
                        s3Service,
                        svgGenerationService).produceMap());

                if(reportMap.get("isolationPointSubreport") == null) {
                    logger.warn("Long Form Report for ProcedureDefinition with ID " + procedureDefinition.getId() +
                            " is missing the Isolation Points Subreport... did you expect this?");
                }

                File tempReport = doPrint((InputStream)reportMap.get("main"), reportMap);

                String downloadName = procedureDefinition.getProcedureCode() + "_-_Long";

                downloadName = downloadName.replaceAll(" ", "_");

                handleDownload(tempReport, downloadName);
            }
        };
        longLink.add(new Label("label", new FIDLabelModel("label.long_form")));
        optionsContainer2.add(longLink);
        add(optionsContainer2);
        optionsContainer2.setVisible(!procedureDefinition.getPublishedState().equals(PublishedState.REJECTED) && actionsList.isVisible());

    }

    /**
     * Unfortunately, we can't directly apply custom logic to the onClick method of a download link.  This is because
     * most of the actual "download" logic in the DownloadLink class happens in that method.
     *
     * So instead, we've brought the download link logic here.
     *
     * @param tempReport - A <b>File</b> pointing to the report.
     * @param fileName - A <b>String</b> value specifying the name of the file that will be sent back in the response to the client.
     */
    private void handleDownload(File tempReport, String fileName) {
        String encodedFileName = UrlEncoder.QUERY_INSTANCE.encode(fileName, getRequest().getCharset()) + ".pdf";

        IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(tempReport));

        Duration cacheDuration = Duration.minutes(5);

        getRequestCycle().scheduleRequestHandlerAfterCurrent(
                new ResourceStreamRequestHandler(resourceStream) {
                    @Override
                    public void respond(IRequestCycle requestCycle) {
                        super.respond(requestCycle);
                    }
                }.setFileName(encodedFileName).setContentDisposition(ContentDisposition.ATTACHMENT).setCacheDuration(cacheDuration)
        );
    }

    private File doPrint(InputStream reportBody, HashMap<String, Object> reportMap) {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportBody);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, new JREmptyDataSource());

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

            File outFile = File.createTempFile("temp-file", ".tmp");

            FileOutputStream output = new FileOutputStream(outFile);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();
            output.close();

            return outFile;
        } catch (JRException | IOException e) {
            logger.error("Jasper Report for user " + FieldIDSession.get().getSessionUser().getId() + " when generating" +
                    "a LOTO Short or Long form report.", e);
        }

        return null;
    }

    private boolean isAuthor(ProcedureDefinition procedureDefinition) {

        if (procedureDefinitionService.isCurrentUserAuthor(procedureDefinition)) {
            return true;
        }

        return false;

    }

    private boolean isApprover(ProcedureDefinition procedureDefinition) {

        if (!isAuthor(procedureDefinition) && procedureDefinitionService.canCurrentUserApprove()) {
            return true;
        }
        return false;
    }

    private boolean isRejected(ProcedureDefinition procedureDefinition) {

        if (procedureDefinition.getPublishedState().equals(PublishedState.REJECTED ) && null != procedureDefinition.getRejectedDate()) {
            return true;
        }
        return false;
    }

}

