package com.n4systems.fieldid.wicket.pages.event;


import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.Event;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class EventSummaryPage extends FieldIDTemplatePage {

    protected long uniqueId;
    protected Boolean useContext;
    protected EventSummaryType eventSummaryType;

    protected enum EventSummaryType {THING_EVENT, PLACE_EVENT};

    public EventSummaryPage(PageParameters parameters) {
        uniqueId = parameters.get("id").toLong();
        useContext = parameters.get("useContext").toBoolean();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(getDetailsPanel("detailsPanel"));

        add(getEventFormPanel("eventFormPanel"));

        add(getEventResultPanel("eventResultPanel"));

        add(getPostEventInfoPanel("eventInfoPanel"));

        add(getEventAttachmentsPanel("eventAttachmentsPanel"));

        add(getEventLocationPanel("eventLocationPanel"));

        add(getProofTestPanel("proofTestPanel"));

    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.view_event"));
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pages/event-summary.css");
    }

    protected abstract Panel getDetailsPanel(String id);

    protected abstract Panel getEventFormPanel(String id);

    protected abstract Panel getEventResultPanel(String id);

    protected abstract Panel getPostEventInfoPanel(String id);

    protected abstract Panel getEventAttachmentsPanel(String id);

    protected abstract Panel getEventLocationPanel(String id);

    protected abstract Panel getProofTestPanel(String id);

    protected abstract Event getEvent();

    private class ActionGroup extends Fragment {

        public ActionGroup(String id) {
            super(id, "viewEventsActionGroup", EventSummaryPage.this);

            if(eventSummaryType.equals(EventSummaryType.THING_EVENT))
                add(new BookmarkablePageLink<EditEventPage>("editLink", EditEventPage.class, PageParametersBuilder.uniqueId(uniqueId)));
            else
                add(new BookmarkablePageLink<EditEventPage>("editLink", EditPlaceEventPage.class, PageParametersBuilder.uniqueId(uniqueId)));

            WebMarkupContainer printDropDown;
            add(printDropDown = new WebMarkupContainer("printDropDown"));
            printDropDown.add(new NonWicketLink("printInspectionCertLink", "file/downloadEventCert.action?uniqueID="+uniqueId+"&reportType=INSPECTION_CERT")
                    .add(new Label("label", new FIDLabelModel("label.pdfreport", getEvent().getType().getGroup().getReportTitle()))).setVisible(getEvent().isEventCertPrintable()));
            printDropDown.add(new NonWicketLink("printObservationCertLink", "file/downloadEventCert.action?uniqueID="+uniqueId+"&reportType=OBSERVATION_CERT").setVisible(getEvent().isObservationCertPrintable()));
            printDropDown.setVisible(getEvent().getType().isThingEventType() && (getEvent().isEventCertPrintable() || getEvent().isObservationCertPrintable()));
        }
    }

}
