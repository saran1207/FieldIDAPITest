package com.n4systems.fieldid.wicket.components.massupdate;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.model.asset.MassUpdateAssetModel;
import com.n4systems.model.search.AssetSearchCriteriaModel;

public class MassUpdateAssetsPanel extends Panel {
	
	private AbstractMassUpdatePanel currentPanel;
	private MassUpdateNavigationPanel navPanel;
	
	protected enum MassUpdateOperation { Edit, Delete; };
	
	public MassUpdateAssetsPanel(String id, final IModel<AssetSearchCriteriaModel> assetSearchCriteria) {
		super(id);
		
		currentPanel = new SelectOperationPanel("massUpdatePanel", assetSearchCriteria) {
			@Override
			protected void onOperationSelected(MassUpdateOperation operation) {
				if(operation == MassUpdateOperation.Delete) {
					this.replaceWith(currentPanel = getDeleteDetailsPanel(assetSearchCriteria, currentPanel));
				} else {
					this.replaceWith(currentPanel = getEditDetailsPanel(assetSearchCriteria, currentPanel));
				}
				updateNavigationPanel(assetSearchCriteria, currentPanel);
			}
		};
		add(currentPanel);
		
		add(navPanel = getNavigationPanel(assetSearchCriteria, currentPanel));
	}
	
	private MassUpdateNavigationPanel getNavigationPanel(IModel<AssetSearchCriteriaModel> assetSearchCriteria, AbstractMassUpdatePanel panel) {
		return new MassUpdateNavigationPanel("navPanel", assetSearchCriteria, panel);
	}
	
	private void updateNavigationPanel(IModel<AssetSearchCriteriaModel> assetSearchCriteria, AbstractMassUpdatePanel panel) {
		navPanel.setParent(this);
		navPanel.replaceWith(getNavigationPanel(assetSearchCriteria, panel));		
	}

	private DeleteDetailsPanel getDeleteDetailsPanel(final IModel<AssetSearchCriteriaModel> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		return new DeleteDetailsPanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				this.replaceWith(currentPanel = getPreviousPanel());
				updateNavigationPanel(assetSearchCriteria, getPreviousPanel());
			}
			
			@Override
			protected void onNext() {
				this.replaceWith(currentPanel = getConfirmDeletePanel(assetSearchCriteria, currentPanel));
				updateNavigationPanel(assetSearchCriteria, currentPanel);
			}
		};
	}
	
	private AbstractMassUpdatePanel getEditDetailsPanel(final IModel<AssetSearchCriteriaModel> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		return new EditDetailsPanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				this.replaceWith(currentPanel = getPreviousPanel());
				updateNavigationPanel(assetSearchCriteria, getPreviousPanel());
			}
			
			@Override
			protected void onNext(MassUpdateAssetModel massUpdateAssetModel) {
				this.replaceWith(currentPanel = getConfirmEditPanel(assetSearchCriteria, currentPanel, massUpdateAssetModel));
				updateNavigationPanel(assetSearchCriteria, currentPanel);
			}

		};
	}

	private ConfirmDeletePanel getConfirmDeletePanel(final IModel<AssetSearchCriteriaModel> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		return new ConfirmDeletePanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				this.replaceWith(currentPanel = getPreviousPanel());
				updateNavigationPanel(assetSearchCriteria, getPreviousPanel());
			}
		};
	}
	
	private ConfirmEditPanel getConfirmEditPanel(final IModel<AssetSearchCriteriaModel> assetSearchCriteria, AbstractMassUpdatePanel previousPanel, MassUpdateAssetModel massUpdateAssetModel) {
		return new ConfirmEditPanel("massUpdatePanel", assetSearchCriteria, previousPanel, massUpdateAssetModel){
			@Override
			protected void onCancel() {
				this.replaceWith(currentPanel = getPreviousPanel());
				updateNavigationPanel(assetSearchCriteria, getPreviousPanel());
			}			
		};
	}

	public void setCurrentPanel(AbstractMassUpdatePanel panel) {
		this.currentPanel = panel;
	}


}
