package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.reporting.results.ReportingMassActionLink;
import com.n4systems.fieldid.wicket.components.reporting.results.ScheduleMassActionLink;
import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.pages.print.ExportToExcelPage;
import com.n4systems.fieldid.wicket.pages.print.PrintInspectionCertPage;
import com.n4systems.fieldid.wicket.pages.print.PrintObservationCertReportPage;
import com.n4systems.fieldid.wicket.pages.print.PrintThisReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.summary.EventResolutionPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventStatus;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;


public abstract class ReportingSubMenu extends SubMenu<EventReportCriteria> {

    private Link printLink;
    private Link exportLink;
    private Link updateLink;
    private Link assignJobLink;
    private Link updateSchedulesLink;
    private WebMarkupContainer actions;
    private WebMarkupContainer print;


    public ReportingSubMenu(String id, final Model<EventReportCriteria> model) {
		super(id, model);

        add(exportLink = makeLinkLightBoxed(new MassActionLink<ExportToExcelPage>("exportToExcelLink", ExportToExcelPage.class, model)));

        actions = new WebMarkupContainer("actions");
        // note that only one of these mass update links will be shown at a time - depends on the context.
        actions.add(updateLink = new ReportingMassActionLink("massUpdateLink", "/massUpdateEvents.action?searchId=%s", model));
        actions.add(updateSchedulesLink = new ScheduleMassActionLink("massScheduleUpdateLink", "/massUpdateEventSchedule.action?searchId=%s", model));
        actions.add(assignJobLink = new ReportingMassActionLink("assignJobLink", "/selectJobToAssignEventsTo.action?searchId=%s&reportType=OBSERVATION_CERT", model));
        add(actions);

        print = new WebMarkupContainer("print");
        print.add(makeLinkLightBoxed(new MassActionLink<PrintThisReportPage>("printThisReportLink", PrintThisReportPage.class, model)));
        print.add(makeLinkLightBoxed(new MassActionLink<PrintInspectionCertPage>("printSelectedPdfReportsLink", PrintInspectionCertPage.class, model)));
        print.add(makeLinkLightBoxed(new MassActionLink<PrintObservationCertReportPage>("printSelectedObservationReportsLink", PrintObservationCertReportPage.class, model)));
        add(print);

        add(new Link("summaryReportLink") {
            @Override public void onClick() {
                setResponsePage(new EventResolutionPage(model));
            }
        });

        
        WebMarkupContainer saveMenu = createSaveMenu("saveMenu");
        
        initializeLimits();
    }

    protected Link createSaveAsLink(String id) {
        throw new IllegalStateException("you must override this method to create Save As link for the SubMenu");
    }

    protected Link createSaveLink(String id) {
        throw new IllegalStateException("you must override this method to create Save link for the SubMenu");
    }

    @Override
    protected void updateMenuBeforeRender(int selected) {
        super.updateMenuBeforeRender(selected);  // update
        exportLink.setVisible(selected > 0 && selected < maxExport);
        print.setVisible(selected > 0 && selected < maxPrint);
        actions.setVisible(selected > 0 && selected < maxUpdate);
        EventStatus status = model.getObject().getEventStatus();
        updateLink.setVisible(status==EventStatus.COMPLETE);
        updateSchedulesLink.setVisible(status==EventStatus.INCOMPLETE);
    }

    @Override
    protected String getNoneSelectedMsgKey() {
        return "label.select_events";
    }

}
