package com.n4systems.fieldid.wicket.components.massupdate;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.massupdate.asset.EditDetailsPanel;
import com.n4systems.model.search.SearchCriteria;

public class MassUpdateNavigationPanel extends Panel {

	protected Link selectOperationLink;
	protected Link operationDetailsLink;
	protected WebMarkupContainer backToSelectOperationLabel;
	protected WebMarkupContainer backToOperationDetailsLabel;
	protected WebMarkupContainer confirmLabel;

	public MassUpdateNavigationPanel(String id, final SearchCriteria searchCriteria, final AbstractMassUpdatePanel panel) {
		super(id);
		
		add(new Link("backToSearch") {
			@Override
			public void onClick() {
				onBackToSearch(searchCriteria);
			}
		});
		
		add(backToSelectOperationLabel = new WebMarkupContainer("backToSelectOperationLabel"));
		add(backToOperationDetailsLabel = new WebMarkupContainer("backToOperationDetailsLabel"));
		add(confirmLabel = new WebMarkupContainer("confirmLabel"));
		
		if(panel.isDetailsPanel()) {
			add(selectOperationLink = createLink("backToSelectOperation", panel));
			backToSelectOperationLabel.setVisible(false);
			add(operationDetailsLink = createBlankLink("backToOperationDetails"));
			operationDetailsLink.setVisible(false);
			backToOperationDetailsLabel.add(new AttributeModifier("class", "strong"));
			if(panel.isDetailsPanel()) {
				add(new AttributeModifier("class", "navMenu navMenuLong"));
			}else {
				add(new AttributeModifier("class", "navMenu navMenuShort"));
			}
		} else if(panel.isConfirmPanel()) {
			add(selectOperationLink = createLink("backToSelectOperation", panel.getPreviousPanel()));
			backToSelectOperationLabel.setVisible(false);			
			add(operationDetailsLink = createLink("backToOperationDetails", panel));
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

	protected Link createLink(String string, AbstractMassUpdatePanel panel){
		return createBlankLink("");
	};
	
	protected void onBackToSearch(SearchCriteria searchCriteria) {};

	protected Link createBlankLink(String id) {
		return new Link(id) {
			@Override
			public void onClick() {
			}
		};		
	}
	
	

}