package com.n4systems.fieldid.wicket.components.massupdate;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.model.search.AssetSearchCriteriaModel;

public class MassUpdateAssetsPanel extends Panel {
	
	private Panel currentPanel;
	
	protected enum MassUpdateOperation { Edit, Delete; };
	
	public MassUpdateAssetsPanel(String id, final IModel<AssetSearchCriteriaModel> assetSearchCriteria) {
		super(id);
		
		currentPanel = new SelectOperationPanel("massUpdatePanel", assetSearchCriteria) {
			@Override
			protected void onOperationSelected(MassUpdateOperation operation) {
				if(operation == MassUpdateOperation.Delete) {
					currentPanel.replaceWith(currentPanel = getDeleteDetailsPanel(assetSearchCriteria, currentPanel));
				} else {
					currentPanel.replaceWith(currentPanel = getEditDetailsPanel(assetSearchCriteria, currentPanel));
				}
			}
		};
		add(currentPanel);
		
	}
	
	private DeleteDetailsPanel getDeleteDetailsPanel(final IModel<AssetSearchCriteriaModel> assetSearchCriteria, Panel previousPanel) {
		return new DeleteDetailsPanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				this.replaceWith(currentPanel = getPreviousPanel());
			}
			
			@Override
			protected void onNext() {
				this.replaceWith(currentPanel = getConfirmDeletePanel(assetSearchCriteria, currentPanel));
			}
		};
	}
	
	private EditDetailsPanel getEditDetailsPanel(IModel<AssetSearchCriteriaModel> assetSearchCriteria, Panel previousPanel) {
		return new EditDetailsPanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				this.replaceWith(currentPanel = getPreviousPanel());
			}
		};
	}
	
	private ConfirmDeletePanel getConfirmDeletePanel(IModel<AssetSearchCriteriaModel> assetSearchCriteria, Panel previousPanel) {
		return new ConfirmDeletePanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				this.replaceWith(currentPanel = getPreviousPanel());
			}
		};
		
	}

}
