package com.n4systems.fieldid.wicket.components.massupdate.openevent;

import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateNavigationPanel;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateOperation;
import com.n4systems.fieldid.wicket.components.massupdate.SelectOperationPanel;
import com.n4systems.fieldid.wicket.components.massupdate.event.ConfirmDeletePanel;
import com.n4systems.fieldid.wicket.components.massupdate.event.ConfirmEditPanel;
import com.n4systems.fieldid.wicket.components.massupdate.event.DeleteDetailsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.Arrays;

public class MassUpdateOpenEventsPanel extends Panel {

    private AbstractMassUpdatePanel currentPanel;
    private MassUpdateNavigationPanel navPanel;


    public MassUpdateOpenEventsPanel(String id, final IModel<EventReportCriteria> criteriaModel) {
        super(id);

        currentPanel = new SelectOperationPanel("massUpdatePanel", (SearchCriteria) criteriaModel.getObject(), Arrays.asList(MassUpdateOperation.values()), new FIDLabelModel("label.open_events").getObject()) {
            @Override
            protected void onOperationSelected(MassUpdateOperation operation) {
                if(operation == MassUpdateOperation.DELETE) {
                    this.replaceWith(currentPanel = getDeleteDetailsPanel(criteriaModel, currentPanel));
                }else if (operation == MassUpdateOperation.CLOSE) {
                    this.replaceWith(currentPanel = getCloseDetailsPanel(criteriaModel, currentPanel));
                } else {
                    this.replaceWith(currentPanel = getEditDetailsPanel(criteriaModel, currentPanel));
                }
                updateNavigationPanel(criteriaModel, currentPanel);
            }
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage(criteriaModel.getObject()));
            }
        };

        add(currentPanel);

        add(navPanel = getNavigationPanel(criteriaModel, currentPanel));

    }

    private EditDetailsPanel getEditDetailsPanel(final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel previousPanel) {
        return new EditDetailsPanel("massUpdatePanel", criteriaModel, previousPanel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)criteriaModel.getObject()));
            }

            @Override
            protected void onNext(MassUpdateEventModel massUpdateEventModel) {
                this.replaceWith( currentPanel = getConfirmEditPanel(criteriaModel, currentPanel, massUpdateEventModel));
                updateNavigationPanel(criteriaModel, currentPanel);
            }
        };
    }

    private AbstractMassUpdatePanel getConfirmEditPanel(final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel currentPanel, MassUpdateEventModel massUpdateEventModel) {
        return new ConfirmEditPanel("massUpdatePanel", criteriaModel, currentPanel, massUpdateEventModel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)criteriaModel.getObject()));
            }
        };
    }

    private DeleteDetailsPanel getDeleteDetailsPanel(final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel previousPanel) {
        return new DeleteDetailsPanel("massUpdatePanel", criteriaModel, previousPanel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)criteriaModel.getObject()));
            }

            @Override
            protected void onNext() {
                this.replaceWith(currentPanel = getConfirmDeletePanel(criteriaModel, currentPanel));
                updateNavigationPanel(criteriaModel, currentPanel);
            }
        };
    }

    private ConfirmDeletePanel getConfirmDeletePanel(final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel previousPanel) {
        return new ConfirmDeletePanel("massUpdatePanel", criteriaModel, previousPanel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)criteriaModel.getObject()));
            }
        };
    }


    private CloseDetailsPanel getCloseDetailsPanel(final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel previousPanel) {
        return new CloseDetailsPanel("massUpdatePanel", criteriaModel, previousPanel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)criteriaModel.getObject()));
            }

            @Override
            protected void onNext(MassUpdateEventModel massUpdateEventModel) {
                this.replaceWith( currentPanel = getConfirmClosePanel(criteriaModel, currentPanel, massUpdateEventModel));
                updateNavigationPanel(criteriaModel, currentPanel);
            }
        };
    }

    private ConfirmClosePanel getConfirmClosePanel(final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel previousPanel, MassUpdateEventModel massUpdateEventModel) {
        return new ConfirmClosePanel("massUpdatePanel", criteriaModel, previousPanel, massUpdateEventModel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)criteriaModel.getObject()));
            }
        };
    }
            
    private void updateNavigationPanel(IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel panel) {
        navPanel.setParent(this);
        navPanel.replaceWith(getNavigationPanel(criteriaModel, panel));
    }

    private MassUpdateNavigationPanel getNavigationPanel(final IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel panel) {
        return new MassUpdateNavigationPanel("navPanel", eventSearchCriteria.getObject(), panel){
            @Override
            protected void onBackToSearch(SearchCriteria searchCriteria) {
                setResponsePage(new ReportPage((EventReportCriteria)searchCriteria));
            }

            @Override
            protected Link createLink(String id, final AbstractMassUpdatePanel panel) {
                return new Link(id) {
                    @Override
                    public void onClick() {
                        AbstractMassUpdatePanel previousPanel = panel.getPreviousPanel();
                        panel.setParent(this.getParent().getParent());
                        panel.replaceWith(previousPanel);
                        ((MassUpdateOpenEventsPanel) this.getParent().getParent()).setCurrentPanel(previousPanel);
                        this.getParent().replaceWith(getNavigationPanel(eventSearchCriteria, previousPanel));
                    }
                };
            }
        };
    }

    public void setCurrentPanel(AbstractMassUpdatePanel panel) {
        this.currentPanel = panel;

    }


}
