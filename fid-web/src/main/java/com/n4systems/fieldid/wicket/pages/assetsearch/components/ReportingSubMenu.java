package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.AssignEventsToJobPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateEventsPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateOpenEventsPage;
import com.n4systems.fieldid.wicket.pages.print.ExportReportToExcelPage;
import com.n4systems.fieldid.wicket.pages.print.PrintInspectionCertPage;
import com.n4systems.fieldid.wicket.pages.print.PrintObservationCertReportPage;
import com.n4systems.fieldid.wicket.pages.print.PrintThisReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.summary.EventResolutionPage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.AddSendSavedItemPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventSearchType;
import com.n4systems.model.search.WorkflowStateCriteria;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import rfid.web.helper.SessionUser;


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


        actions = new WebMarkupContainer("actions");

        actions.add(exportLink = makeLinkLightBoxed(new MassActionLink<ExportReportToExcelPage>("exportToExcelLink", ExportReportToExcelPage.class, model)));

        // note that only one of these mass update links will be shown at a time - depends on the context.
        actions.add(assignJobLink = new MassActionLink<AssignEventsToJobPage>("assignJobLink", AssignEventsToJobPage.class, model));

        actions.add(updateLink = new Link("massUpdateLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassUpdateEventsPage(model));
            }
        });

        actions.add(updateSchedulesLink = new Link("massScheduleUpdateLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassUpdateOpenEventsPage(model));
            }
        });

        add(new Link("summaryReportLink") {
            @Override public void onClick() {
                setResponsePage(new EventResolutionPage(model));
            }
        });

        add(actions);

        print = new WebMarkupContainer("print");
        print.add(makeLinkLightBoxed(new MassActionLink<PrintThisReportPage>("printThisReportLink", PrintThisReportPage.class, model)));
        print.add(makeLinkLightBoxed(new MassActionLink<PrintInspectionCertPage>("printSelectedPdfReportsLink", PrintInspectionCertPage.class, model)));
        print.add(makeLinkLightBoxed(new MassActionLink<PrintObservationCertReportPage>("printSelectedObservationReportsLink", PrintObservationCertReportPage.class, model)));
        add(print);
        add(new Link("emailLink") {
            @Override public void onClick() {
                setResponsePage(new AddSendSavedItemPage(model, getPage()));
            }
        });
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
    protected void updateMenuBeforeRender(EventReportCriteria criteria) {
        super.updateMenuBeforeRender(criteria);
        int selected = criteria.getSelection().getNumSelectedIds();

        exportLink.setVisible(selected > 0 && selected < maxExport);
        print.setVisible(selected > 0 && selected < maxPrint && !isSearchForClosed(criteria) && !isSearchForActions(criteria));
        
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        boolean searchIncludesSafetyNetwork = model.getObject().isIncludeSafetyNetwork();
        WorkflowStateCriteria state = model.getObject().getWorkflowState();
        
        updateLink.setVisible(state == WorkflowStateCriteria.COMPLETE && sessionUser.hasAccess("editevent"));
        updateSchedulesLink.setVisible(state == WorkflowStateCriteria.OPEN && sessionUser.hasAccess("editevent"));

        assignJobLink.setVisible(FieldIDSession.get().getSecurityGuard().isProjectsEnabled() && sessionUser.hasAccess("createevent") && !searchIncludesSafetyNetwork);

        actions.setVisible(selected > 0 && selected < maxUpdate && (updateLink.isVisible() || updateSchedulesLink.isVisible() || assignJobLink.isVisible() || exportLink.isVisible()));
    }

    private boolean isSearchForClosed(EventReportCriteria criteria) {
        return WorkflowStateCriteria.CLOSED.equals(criteria.getWorkflowState());
    }

    private boolean isSearchForActions(EventReportCriteria criteria) {
        return EventSearchType.ACTIONS.equals(criteria.getEventSearchType());
    }

    @Override
    protected String getNoneSelectedMsgKey() {
        return "label.select_events";
    }

}
