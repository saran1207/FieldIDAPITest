package com.n4systems.fieldid.wicket.components.massupdate;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateAssetsPanel.MassUpdateOperation;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

public class SelectOperationPanel extends Panel {

	private List<String> operations = Arrays.asList(new String[] {"Edit", "Delete"});

	private String selected;
	
	public SelectOperationPanel(String id, final IModel<AssetSearchCriteriaModel> assetSearchCriteria) {
		super(id);
		
		RadioChoice<String> optType = new RadioChoice<String>("operations", new PropertyModel<String>(this, "selected"), operations);
		
		Form<Void> massUpdateForm = new Form<Void>("form"){
			@Override
			protected void onSubmit() {
				onOperationSelected(MassUpdateOperation.valueOf(selected));
			}
		};
		
		add(massUpdateForm);
		massUpdateForm.add(optType);
		massUpdateForm.add(new Label("selectOperationMessage", new FIDLabelModel("message.mass_update_select_operation", 
				assetSearchCriteria.getObject().getSelection().getNumSelectedIds())));		
		massUpdateForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
				setResponsePage(new AssetSearchResultsPage(assetSearchCriteria.getObject()));
			}
		});
	}
	
	protected void onOperationSelected(MassUpdateOperation operation) {}
	
}
