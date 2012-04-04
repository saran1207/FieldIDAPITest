package com.n4systems.fieldid.wicket.components.massupdate.event;

import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateOperation;
import com.n4systems.fieldid.wicket.components.massupdate.SelectOperationPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class MassUpdateEventsPanel extends Panel {

    private AbstractMassUpdatePanel currentPanel;
    //private MassUpdateNavigationPanel navPanel;

    public MassUpdateEventsPanel(String id, final IModel<EventReportCriteria> eventSearchCriteria) {
        super(id);
        currentPanel = new SelectOperationPanel("massUpdatePanel", (SearchCriteria) eventSearchCriteria.getObject(), new FIDLabelModel("label.events.lc").getObject())  {
            @Override
            protected void onOperationSelected(MassUpdateOperation operation) {
                if(operation == MassUpdateOperation.DELETE) {
                    this.replaceWith(currentPanel = getDeleteDetailsPanel(eventSearchCriteria, currentPanel));
                } else {
                    //this.replaceWith(currentPanel = getEditDetailsPanel(assetSearchCriteria, currentPanel));
                }
                //updateNavigationPanel(assetSearchCriteria, currentPanel);
            }  
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage(eventSearchCriteria.getObject()));
            }
        };
        add(currentPanel);
    }


    private DeleteDetailsPanel getDeleteDetailsPanel(final IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel) {
        return new DeleteDetailsPanel("massUpdatePanel", eventSearchCriteria, previousPanel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)eventSearchCriteria.getObject()));
            }

            @Override
            protected void onNext() {
                this.replaceWith(currentPanel = getConfirmDeletePanel(eventSearchCriteria, currentPanel));
                //updateNavigationPanel(eventSearchCriteria, currentPanel);
            }
        };
    }

    private ConfirmDeletePanel getConfirmDeletePanel(final IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel) {
        return new ConfirmDeletePanel("massUpdatePanel", eventSearchCriteria, previousPanel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)eventSearchCriteria.getObject()));
            }
        };
    }
}
