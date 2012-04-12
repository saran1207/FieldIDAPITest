package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.reporting.results.ReportingMassActionLink;
import com.n4systems.fieldid.wicket.components.reporting.results.ScheduleMassActionLink;
import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateEventsPage;
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
    private Link summaryReportLink;


    public ReportingSubMenu(String id, final Model<EventReportCriteria> model) {
		super(id, model);

        add(exportLink = makeLinkLightBoxed(new MassActionLink<ExportToExcelPage>("exportToExcelLink", ExportToExcelPage.class, model)));

        actions = new WebMarkupContainer("actions");
        // note that only one of these mass update links will be shown at a time - depends on the context.
        actions.add(updateSchedulesLink = new ScheduleMassActionLink("massScheduleUpdateLink", "/massUpdateEventSchedule.action?searchId=%s", model));
        actions.add(assignJobLink = new ReportingMassActionLink("assignJobLink", "/selectJobToAssignEventsTo.action?searchId=%s&reportType=OBSERVATION_CERT", model));

        actions.add(updateLink = new Link("massUpdateLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassUpdateEventsPage(model));
            }
        });

        add(actions);

        print = new WebMarkupContainer("print");
        print.add(makeLinkLightBoxed(new MassActionLink<PrintThisReportPage>("printThisReportLink", PrintThisReportPage.class, model)));
        print.add(makeLinkLightBoxed(new MassActionLink<PrintInspectionCertPage>("printSelectedPdfReportsLink", PrintInspectionCertPage.class, model)));
        print.add(makeLinkLightBoxed(new MassActionLink<PrintObservationCertReportPage>("printSelectedObservationReportsLink", PrintObservationCertReportPage.class, model)));
        add(print);

        add(summaryReportLink = new Link("summaryReportLink") {
            @Override public void onClick() {
                setResponsePage(new EventResolutionPage(model));
            }
        });

        add(new WebMarkupContainer("emailLink"));
        add(new SaveMenu("saveMenu") {
            @Override protected Link createSaveLink(String id) {
                return ReportingSubMenu.this.createSaveLink(id);
            }
            @Override protected Link createSaveAsLink(String id) {
                return ReportingSubMenu.this.createSaveAsLink(id);
            }
        });

        initializeLimits();
    }

    @Override
    protected void updateMenuBeforeRender(int selected) {
        super.updateMenuBeforeRender(selected);  // update
        summaryReportLink.setVisible(selected>0);
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
