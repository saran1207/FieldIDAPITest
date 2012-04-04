package com.n4systems.fieldid.wicket.components.massupdate;

import com.n4systems.fieldid.wicket.components.massupdate.asset.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteria;

public class MassUpdateNavigationPanel extends Panel {
	
	private Link selectOperationLink;
	private Link operationDetailsLink;
	private WebMarkupContainer backToSelectOperationLabel;
	private WebMarkupContainer backToOperationDetailsLabel;
	private WebMarkupContainer confirmLabel;
	
	public MassUpdateNavigationPanel(String id, final IModel<AssetSearchCriteria> assetSearchCriteria, final AbstractMassUpdatePanel panel) {
		super(id, assetSearchCriteria);

		add(new Link("backToSearch") {
			@Override
			public void onClick() {
				setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
			}
		});
		
		add(backToSelectOperationLabel = new WebMarkupContainer("backToSelectOperationLabel"));
		add(backToOperationDetailsLabel = new WebMarkupContainer("backToOperationDetailsLabel"));
		add(confirmLabel = new WebMarkupContainer("confirmLabel"));
		
		if(panel instanceof EditDetailsPanel || panel instanceof DeleteDetailsPanel) {
			add(selectOperationLink = createLink("backToSelectOperation", assetSearchCriteria, panel));
			backToSelectOperationLabel.setVisible(false);
			add(operationDetailsLink = createBlankLink("backToOperationDetails"));
			operationDetailsLink.setVisible(false);
			backToOperationDetailsLabel.add(new AttributeModifier("class", "strong"));
			if(panel instanceof EditDetailsPanel) {
				add(new AttributeModifier("class", "navMenu navMenuLong"));
			}else {
				add(new AttributeModifier("class", "navMenu navMenuShort"));
			}
		} else if(panel instanceof ConfirmEditPanel || panel instanceof ConfirmDeletePanel) {
			add(selectOperationLink = createLink("backToSelectOperation", assetSearchCriteria, panel.getPreviousPanel()));
			backToSelectOperationLabel.setVisible(false);			
			add(operationDetailsLink = createLink("backToOperationDetails", assetSearchCriteria, panel));
			backToOperationDetailsLabel.setVisible(false);
			confirmLabel.add(new AttributeModifier("class", "strong"));
			add(new AttributeModifier("class", "navMenu navMenuShort"));
		} else {
			add(selectOperationLink = createBlankLink("backToSelectOperation"));
			selectOperationLink.setVisible(false);
			add(operationDetailsLink = createBlankLink("backToOperationDetails"));
			operationDetailsLink.setVisible(false);
			backToSelectOperationLabel.add(new AttributeModifier("class", "strong"));
			add(new AttributeModifier("class", "navMenu navMenuShort"));
		}		
	}
	
	private Link createLink(String id, final IModel<AssetSearchCriteria> assetSearchCriteria, final AbstractMassUpdatePanel panel) {
		return new Link(id) {
			@Override
			public void onClick() {
				AbstractMassUpdatePanel previousPanel = panel.getPreviousPanel();
				panel.setParent(this.getParent().getParent());
				panel.replaceWith(previousPanel);
				((MassUpdateAssetsPanel) this.getParent().getParent()).setCurrentPanel(previousPanel);
				this.getParent().replaceWith(new MassUpdateNavigationPanel(this.getParent().getId(), assetSearchCriteria, previousPanel));
			}
		};
	}
	
	private Link createBlankLink(String id) {
		return new Link(id) {
			@Override
			public void onClick() {
			}
		};

		
	}

}
