package com.n4systems.fieldid.wicket.components.massupdate.asset;

import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateNavigationPanel;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateOperation;
import com.n4systems.fieldid.wicket.components.massupdate.SelectOperationPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.model.asset.MassUpdateAssetModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteria;

public class MassUpdateAssetsPanel extends Panel {
	
	private AbstractMassUpdatePanel currentPanel;
	private MassUpdateNavigationPanel navPanel;
		
	public MassUpdateAssetsPanel(String id, final IModel<AssetSearchCriteria> assetSearchCriteria) {
		super(id);
		
		currentPanel = new SelectOperationPanel("massUpdatePanel", (SearchCriteria) assetSearchCriteria.getObject(), new FIDLabelModel("label.assets.lc").getObject()) {
			@Override
			protected void onOperationSelected(MassUpdateOperation operation) {
				if(operation == MassUpdateOperation.DELETE) {
					this.replaceWith(currentPanel = getDeleteDetailsPanel(assetSearchCriteria, currentPanel));
				} else {
					this.replaceWith(currentPanel = getEditDetailsPanel(assetSearchCriteria, currentPanel));
				}
				updateNavigationPanel(assetSearchCriteria, currentPanel);
			}

            @Override
            protected void onCancel() {
                setResponsePage(new AssetSearchResultsPage((AssetSearchCriteria)assetSearchCriteria.getObject()));
            }

		};
		add(currentPanel);
		
		add(navPanel = getNavigationPanel(assetSearchCriteria, currentPanel));
	}
	
	private MassUpdateNavigationPanel getNavigationPanel(IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel panel) {
		return new MassUpdateNavigationPanel("navPanel", assetSearchCriteria, panel);
	}
	
	private void updateNavigationPanel(IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel panel) {
		navPanel.setParent(this);
		navPanel.replaceWith(getNavigationPanel(assetSearchCriteria, panel));		
	}

	private DeleteDetailsPanel getDeleteDetailsPanel(final IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		return new DeleteDetailsPanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
			}
			
			@Override
			protected void onNext() {
				this.replaceWith(currentPanel = getConfirmDeletePanel(assetSearchCriteria, currentPanel));
				updateNavigationPanel(assetSearchCriteria, currentPanel);
			}
		};
	}
	
	private AbstractMassUpdatePanel getEditDetailsPanel(final IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		return new EditDetailsPanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
			}
			
			@Override
			protected void onNext(MassUpdateAssetModel massUpdateAssetModel) {
				this.replaceWith(currentPanel = getConfirmEditPanel(assetSearchCriteria, currentPanel, massUpdateAssetModel));
				updateNavigationPanel(assetSearchCriteria, currentPanel);
			}

		};
	}

	private ConfirmDeletePanel getConfirmDeletePanel(final IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel previousPanel) {
		return new ConfirmDeletePanel("massUpdatePanel", assetSearchCriteria, previousPanel) {
			@Override
			protected void onCancel() {
				setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
			}
		};
	}
	
	private ConfirmEditPanel getConfirmEditPanel(final IModel<AssetSearchCriteria> assetSearchCriteria, AbstractMassUpdatePanel previousPanel, MassUpdateAssetModel massUpdateAssetModel) {
		return new ConfirmEditPanel("massUpdatePanel", assetSearchCriteria, previousPanel, massUpdateAssetModel){
			@Override
			protected void onCancel() {
				setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
			}			
		};
	}

	public void setCurrentPanel(AbstractMassUpdatePanel panel) {
		this.currentPanel = panel;
	}


}
