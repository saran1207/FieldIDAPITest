package com.n4systems.fieldid.wicket.components.massupdate;

import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateOperation;
import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateOperationChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;

public class SelectOperationPanel extends AbstractMassUpdatePanel {

	private MassUpdateOperation selected;
	
	public SelectOperationPanel(String id, final SearchCriteria searchCriteria, String updateType) {
		super(id);
		
		RadioChoice<MassUpdateOperation> optType = new RadioChoice<MassUpdateOperation>("operations", new PropertyModel<MassUpdateOperation>(this, "selected"), 
				Arrays.asList(MassUpdateOperation.values()), new MassUpdateOperationChoiceRenderer(updateType));
		
		Form<Void> massUpdateForm = new Form<Void>("form"){
			@Override
			protected void onSubmit() {
				onOperationSelected(selected);
			}
		};
		
		add(massUpdateForm);
		massUpdateForm.add(optType);
		massUpdateForm.add(new Label("selectOperationMessage", new FIDLabelModel("message.mass_update_select_operation",
                searchCriteria.getSelection().getNumSelectedIds(), updateType)));
		massUpdateForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
                onCancel();
			}
		});
	}

    

    protected void onOperationSelected(MassUpdateOperation operation) {}

    protected void onCancel() {};

}
