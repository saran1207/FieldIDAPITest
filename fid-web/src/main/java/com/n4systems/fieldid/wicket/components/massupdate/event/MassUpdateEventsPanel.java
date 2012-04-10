package com.n4systems.fieldid.wicket.components.massupdate.event;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateNavigationPanel;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateOperation;
import com.n4systems.fieldid.wicket.components.massupdate.SelectOperationPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;

public class MassUpdateEventsPanel extends Panel {

    private AbstractMassUpdatePanel currentPanel;
    private MassUpdateNavigationPanel navPanel;

    public MassUpdateEventsPanel(String id, final IModel<EventReportCriteria> eventSearchCriteria) {
        super(id);
        currentPanel = new SelectOperationPanel("massUpdatePanel", (SearchCriteria) eventSearchCriteria.getObject(), new FIDLabelModel("label.events.lc").getObject())  {
            @Override
            protected void onOperationSelected(MassUpdateOperation operation) {
                if(operation == MassUpdateOperation.DELETE) {
                    this.replaceWith(currentPanel = getDeleteDetailsPanel(eventSearchCriteria, currentPanel));
                } else {
                    this.replaceWith(currentPanel = getEditDetailsPanel(eventSearchCriteria, currentPanel));
                }
                updateNavigationPanel(eventSearchCriteria, currentPanel);
            }  
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage(eventSearchCriteria.getObject()));
            }
        };
        add(currentPanel);
        
        add(navPanel = getNavigationPanel(eventSearchCriteria, currentPanel));
    }


	private void updateNavigationPanel(IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel panel) {
		navPanel.setParent(this);
		navPanel.replaceWith(getNavigationPanel(eventSearchCriteria, panel));		
	}
	
	private MassUpdateNavigationPanel getNavigationPanel(IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel panel) {
		return new MassUpdateNavigationPanel("navPanel", eventSearchCriteria.getObject(), panel){
			@Override
			protected void onBackToSearch(SearchCriteria searchCriteria) {
				setResponsePage(new ReportPage((EventReportCriteria)searchCriteria));		
			}
			
			@Override
			protected Link createLink(String id, final SearchCriteria eventSearchCriteria, final AbstractMassUpdatePanel panel) {
				return new Link(id) {
					@Override
					public void onClick() {
						AbstractMassUpdatePanel previousPanel = panel.getPreviousPanel();
						panel.setParent(this.getParent().getParent());
						panel.replaceWith(previousPanel);
						((MassUpdateEventsPanel) this.getParent().getParent()).setCurrentPanel(previousPanel);
						this.getParent().replaceWith(new MassUpdateNavigationPanel(this.getParent().getId(), eventSearchCriteria, previousPanel));
					}
				};
			}
		};
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
                updateNavigationPanel(eventSearchCriteria, currentPanel);
            }
        };
    }

    private EditDetailsPanel getEditDetailsPanel(final IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel previousPanel) {
        return new EditDetailsPanel("massUpdatePanel", eventSearchCriteria, previousPanel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)eventSearchCriteria.getObject()));
            }
            
            @Override
            protected void onNext(MassUpdateEventModel massUpdateEventModel) {
            	this.replaceWith( currentPanel = getConfirmEditPanel(eventSearchCriteria, currentPanel, massUpdateEventModel));
            	updateNavigationPanel(eventSearchCriteria, currentPanel);
            }
        };
    }

	private AbstractMassUpdatePanel getConfirmEditPanel(final IModel<EventReportCriteria> eventSearchCriteria, AbstractMassUpdatePanel currentPanel, MassUpdateEventModel massUpdateEventModel) {		
		return new ConfirmEditPanel("massUpdatePanel", eventSearchCriteria, currentPanel, massUpdateEventModel) {
            @Override
            protected void onCancel() {
                setResponsePage(new ReportPage((EventReportCriteria)eventSearchCriteria.getObject()));
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


	public void setCurrentPanel(AbstractMassUpdatePanel panel) {
		this.currentPanel = panel;
		
	}
}
