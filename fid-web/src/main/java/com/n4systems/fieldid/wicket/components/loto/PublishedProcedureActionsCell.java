package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.DraftListAllPage;
import com.n4systems.fieldid.wicket.pages.loto.PreviouslyPublishedListAllPage;
import com.n4systems.fieldid.wicket.pages.loto.RecurringLotoSchedulesPage;
import com.n4systems.fieldid.wicket.pages.loto.UnpublishProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.LotoPrintoutReportMapProducer;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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

/**
 * Created by rrana on 2014-04-11.
 */

public class PublishedProcedureActionsCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private ProcedureService procedureService;

    @SpringBean
    private LotoReportService lotoReportService;

    @SpringBean
    private SvgGenerationService svgGenerationService;

    private final ProcedureDefinition procedureDefinition;

    public PublishedProcedureActionsCell(String id, final IModel<ProcedureDefinition> proDef, final ProcedureListPanel procedureListPanel) {
        super(id);

        procedureDefinition = proDef.getObject();

        Link reviseLink = new Link("reviseLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinition(procedureDefinition);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
            }
        };

        if (procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED)) {
            reviseLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset()));
        } else if (procedureDefinition.getPublishedState().equals(PublishedState.PREVIOUSLY_PUBLISHED)) {
            reviseLink.setVisible(true);
        } else {
            reviseLink.setVisible(false);
        }

        reviseLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.revise"), TipsyBehavior.Gravity.E));

        if(procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED)) {
            reviseLink.add(new Label("label", new FIDLabelModel("label.revise")));
            reviseLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.revise"), TipsyBehavior.Gravity.E));
        } else {
            reviseLink.add(new Label("label", new FIDLabelModel("label.restore")));
            reviseLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.restore"), TipsyBehavior.Gravity.E));
        }

        add(reviseLink);

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        Link copyLink = new Link("copyLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinitionForCopy(procedureDefinition);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
            }
        };
        copyLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));
        copyLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.copy"), TipsyBehavior.Gravity.E));
        copyLink.add(new Label("label", new FIDLabelModel("label.copy")));
        optionsContainer.add(copyLink);

        boolean showUnpublished = procedureDefinitionService.isApprovalRequired() ? procedureDefinitionService.canCurrentUserApprove() : true;

        AjaxLink unpublishLink = new AjaxLink<Void>("unpublishLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                if(procedureService.hasOpenProcedure(procedureDefinition)) {
                    //Instead of causing an error, we want to forward the user to a new page where they can see the
                    //open procedures for that definition.  We also want to ensure all recurring schedules are removed,
                    //which will only be done after the user confirms this is what they want to do.
                    error(new FIDLabelModel("error.unpublish").getObject());
                    target.add(procedureListPanel.getErrorFeedbackPanel());
                } else {
                    if(procedureDefinitionService.hasRecurringSchedule(procedureDefinition)) {
                        //There's a recurring schedule... the user needs to be notified of this before we proceed.  It's
                        //possible they made a mistake.
                        setResponsePage(new UnpublishProcedureDefinitionPage(
                                        PageParametersBuilder.param("procedureDefinitionId",
                                                procedureDefinition.getId()))
                        );

                    } else {
                        //Otherwise, there's no recurring schedule to warn the user about.  We can just clean it up
                        //right here...
                        procedureDefinitionService.unpublishProcedureDefinition(procedureDefinition);
                        info(new FIDLabelModel("message.unpublish", procedureDefinition.getProcedureCode()).getObject());
                        target.add(procedureListPanel);
                        target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());
                    }
                }
            }
        };
        unpublishLink.setVisible(showUnpublished  && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));
        optionsContainer.add(unpublishLink);

        Link draftLink = new Link("draftLink") {
            @Override
            public void onClick() {
                ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(procedureDefinition.getAsset(), procedureDefinition.getFamilyId());
                setResponsePage(new DraftListAllPage(publishedDef.getProcedureCode(), publishedDef.getAsset(), true, false));
            }
        };
        draftLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));


        optionsContainer.add(draftLink);


        Link previouslyPublishedLink = new Link("previouslyPublishedLink") {
            @Override
            public void onClick() {
                ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(procedureDefinition.getAsset(), procedureDefinition.getFamilyId());
                setResponsePage(new PreviouslyPublishedListAllPage(publishedDef.getProcedureCode(), publishedDef.getAsset(), true, false));
            }
        };
        previouslyPublishedLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));


        optionsContainer.add(previouslyPublishedLink);

        Link recurringSchedulesLink = new Link("recurringSchedulesLink") {
            @Override
            public void onClick() {
                setResponsePage(RecurringLotoSchedulesPage.class, PageParametersBuilder.uniqueId(procedureDefinition.getAsset().getId()));
            }
        };
        recurringSchedulesLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));

        optionsContainer.add(recurringSchedulesLink);


        add(optionsContainer);

        optionsContainer.setVisible(reviseLink.isVisible() || copyLink.isVisible() || unpublishLink.isVisible()
                || draftLink.isVisible() || previouslyPublishedLink.isVisible());


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
                    //TODO Handle this error properly with adequate logging...
                    e.printStackTrace();
                }

                                                                    //TODO Provide proper report title
                reportMap.putAll(new LotoPrintoutReportMapProducer("Report Title",
                                                                    procedureDefinition,
                                                                    LotoPrintoutReportMapProducer.PrintoutType.SHORT,
                                                                    new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault()),
                                                                    s3Service,
                                                                    svgGenerationService).produceMap());

                File tempReport = doPrint((InputStream)reportMap.get("main"), reportMap);

                handleDownload(tempReport, "shortformdownload");
            }
        };

        shortLink.add(new Label("label", new FIDLabelModel("label.short_form")));
        optionsContainer2.add(shortLink);

        Link longLink = new Link("longLink") {
            @Override
            public void onClick() {
                //TODO If the new way works, remove all this shit.
//                File jasper = null;
//                try {
//                    jasper = File.createTempFile("temp-file", ".tmp");
//
//                    FileOutputStream fileAttachmentFos = new FileOutputStream(jasper);
//                    fileAttachmentFos.write(lotoReportService.getLongJapser());
//
//
//                    //jasper = new File(lotoReportService.getLongJapser());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                LotoPrintout printout = lotoReportService.getSelectedLongForm();
//                if(printout == null) {
//                    reportMap.put("SUBREPORT_DIR", PathHandler.getLotoDefaultPath(LotoPrintoutType.LONG) + "/");
//                } else {
//                    reportMap.put("SUBREPORT_DIR", PathHandler.getLotoPath(printout) + "/");
//                }


                HashMap<String, Object> reportMap = Maps.newHashMap();

                try {
                    reportMap.putAll(lotoReportService.getLongJasperMap());
                } catch (IOException e) {
                    //TODO Handle this error properly with adequate logging...
                    e.printStackTrace();
                }

                                                                    //TODO Provide proper report title.
                reportMap.putAll(new LotoPrintoutReportMapProducer("Report Title",
                                                                    procedureDefinition,
                                                                    LotoPrintoutReportMapProducer.PrintoutType.LONG,
                                                                    new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault()),
                                                                    s3Service,
                                                                    svgGenerationService).produceMap());

                File tempReport = doPrint((InputStream)reportMap.get("main"), reportMap);

                handleDownload(tempReport, "longformdownload");
            }
        };
        longLink.add(new Label("label", new FIDLabelModel("label.long_form")));
        optionsContainer2.add(longLink);
        add(optionsContainer2);

    }

    /**
     * Unfortunately, we can't directly apply custom logic to the onClick method of a download link.  This is because
     * most of the actual "download" logic in the DownloadLink class happens in that method.
     *
     * So instead, we've brought the download link logic here.
     *
     * @param tempReport
     * @param fileName
     */
    private void handleDownload(File tempReport, String fileName) {
        String encodedFileName = UrlEncoder.QUERY_INSTANCE.encode(fileName, getRequest().getCharset());

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

    private boolean isAuthor(ProcedureDefinition procedureDefinition) {

        if (procedureDefinitionService.isCurrentUserAuthor(procedureDefinition)) {
            return true;
        }

        return false;

    }

    private File doPrint(File reportTemplate, HashMap<String, Object> reportMap) {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportTemplate);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, new JREmptyDataSource());

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

            //TODO Don't leave me empty!!  Kittens will perish!
            File outFile = File.createTempFile("temp-file", ".tmp");

            FileOutputStream output = new FileOutputStream(outFile);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();
            output.close();

            return outFile;

        } catch (JRException | IOException e) {
            //TODO Do something if this fails with an exception...
            e.printStackTrace();
        }

        return null;
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
            //TODO Do something if this fails with an exception...
            e.printStackTrace();
        }

        return null;
    }
}