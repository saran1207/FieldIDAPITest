package com.n4systems.fieldid.wicket.components.massupdate;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

public class SelectOperationPanel extends AbstractMassUpdatePanel {

	private MassUpdateOperation selected;
	
	public SelectOperationPanel(String id, final IModel<AssetSearchCriteriaModel> assetSearchCriteria) {
		super(id);
		
		RadioChoice<MassUpdateOperation> optType = new RadioChoice<MassUpdateOperation>("operations", new PropertyModel<MassUpdateOperation>(this, "selected"), 
				Arrays.asList(MassUpdateOperation.values()), new MassUpdateOperationChoiceRenderer());
		
		Form<Void> massUpdateForm = new Form<Void>("form"){
			@Override
			protected void onSubmit() {
				onOperationSelected(selected);
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
	
	class MassUpdateOperationChoiceRenderer implements IChoiceRenderer<MassUpdateOperation> {

		@Override
		public Object getDisplayValue(MassUpdateOperation object) {
			return new FIDLabelModel(object.getLabel()).getObject() + " - " + new FIDLabelModel(object.getMessage()).getObject();
		}

		@Override
		public String getIdValue(MassUpdateOperation object, int index) {
			return new FIDLabelModel(object.getLabel()).getObject();
		}
		
	}
	
}
